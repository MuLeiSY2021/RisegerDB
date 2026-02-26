package wal

import (
	"context"
	"log/slog"
	"sync"
	"sync/atomic"
	"time"
)

// Daemon is a background goroutine that periodically triggers database
// persistence and log cleanup after a configurable number of writes.
type Daemon struct {
	threshold  int64
	counter    atomic.Int64
	onFlush    func() error // callback: persist databases and delete logs
	logger     *slog.Logger
	interval   time.Duration
	mu         sync.Mutex
	notifyCh   chan struct{}
}

func NewDaemon(threshold int, interval time.Duration, onFlush func() error, logger *slog.Logger) *Daemon {
	if logger == nil {
		logger = slog.Default()
	}
	if interval <= 0 {
		interval = 30 * time.Second
	}
	return &Daemon{
		threshold: int64(threshold),
		onFlush:   onFlush,
		logger:    logger,
		interval:  interval,
		notifyCh:  make(chan struct{}, 1),
	}
}

// CountDown increments the write counter. If threshold is reached,
// triggers an async flush.
func (d *Daemon) CountDown(n int) {
	v := d.counter.Add(int64(n))
	if v >= d.threshold {
		select {
		case d.notifyCh <- struct{}{}:
		default:
		}
	}
}

// Run starts the daemon loop. It blocks until ctx is cancelled.
func (d *Daemon) Run(ctx context.Context) {
	ticker := time.NewTicker(d.interval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			d.flush()
			return
		case <-d.notifyCh:
			d.flush()
		case <-ticker.C:
			if d.counter.Load() > 0 {
				d.flush()
			}
		}
	}
}

func (d *Daemon) flush() {
	d.mu.Lock()
	defer d.mu.Unlock()

	if err := d.onFlush(); err != nil {
		d.logger.Error("WAL daemon flush failed", "error", err)
		return
	}
	d.counter.Store(0)
	d.logger.Debug("WAL daemon flush completed")
}
