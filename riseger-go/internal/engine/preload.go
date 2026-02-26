package engine

import (
	"fmt"
	"sync"
	"sync/atomic"
	"time"
)

// PreloadTask tracks the status of an async geodata import.
type PreloadTask struct {
	ID        string    `json:"id"`
	File      string    `json:"file"`
	Status    string    `json:"status"` // "pending", "running", "done", "failed"
	Elements  int       `json:"elements,omitempty"`
	Error     string    `json:"error,omitempty"`
	StartedAt time.Time `json:"startedAt"`
	DoneAt    time.Time `json:"doneAt,omitempty"`
}

var taskCounter atomic.Int64

// PreloadManager tracks all preload tasks.
type PreloadManager struct {
	tasks map[string]*PreloadTask
	mu    sync.RWMutex
}

func NewPreloadManager() *PreloadManager {
	return &PreloadManager{tasks: make(map[string]*PreloadTask)}
}

func (pm *PreloadManager) NewTask(file string) *PreloadTask {
	id := fmt.Sprintf("preload_%d", taskCounter.Add(1))
	task := &PreloadTask{
		ID:        id,
		File:      file,
		Status:    "pending",
		StartedAt: time.Now(),
	}
	pm.mu.Lock()
	pm.tasks[id] = task
	pm.mu.Unlock()
	return task
}

func (pm *PreloadManager) GetTask(id string) (*PreloadTask, bool) {
	pm.mu.RLock()
	defer pm.mu.RUnlock()
	t, ok := pm.tasks[id]
	return t, ok
}

func (pm *PreloadManager) ListTasks() []*PreloadTask {
	pm.mu.RLock()
	defer pm.mu.RUnlock()
	result := make([]*PreloadTask, 0, len(pm.tasks))
	for _, t := range pm.tasks {
		result = append(result, t)
	}
	return result
}

// Preload starts an async geodata import. Returns a task ID immediately.
// The actual import runs in a background goroutine.
func (e *Engine) Preload(path string) (string, error) {
	if e.preloadMgr == nil {
		e.preloadMgr = NewPreloadManager()
	}

	task := e.preloadMgr.NewTask(path)
	e.Logger.Info("preload task created", "taskID", task.ID, "file", path)

	go func() {
		task.Status = "running"
		e.Logger.Info("preload task started", "taskID", task.ID)

		count, err := e.ImportGeoData(path)
		task.DoneAt = time.Now()

		if err != nil {
			task.Status = "failed"
			task.Error = err.Error()
			e.Logger.Error("preload task failed", "taskID", task.ID, "error", err)
			return
		}

		task.Elements = count
		task.Status = "done"
		e.Logger.Info("preload task done",
			"taskID", task.ID,
			"elements", count,
			"duration", task.DoneAt.Sub(task.StartedAt),
		)
	}()

	return task.ID, nil
}

// GetPreloadTask returns the status of a preload task by ID.
func (e *Engine) GetPreloadTask(id string) (*PreloadTask, bool) {
	if e.preloadMgr == nil {
		return nil, false
	}
	return e.preloadMgr.GetTask(id)
}
