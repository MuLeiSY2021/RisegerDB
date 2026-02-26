package cache

import "sync"

// CacheManager provides in-memory access to all loaded databases.
type CacheManager struct {
	databases map[string]*Database
	mu        sync.RWMutex
}

func NewCacheManager() *CacheManager {
	return &CacheManager{
		databases: make(map[string]*Database),
	}
}

func (c *CacheManager) AddDatabase(db *Database) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.databases[db.Name] = db
}

func (c *CacheManager) GetDatabase(name string) (*Database, bool) {
	c.mu.RLock()
	defer c.mu.RUnlock()
	db, ok := c.databases[name]
	return db, ok
}

func (c *CacheManager) ListDatabases() []*Database {
	c.mu.RLock()
	defer c.mu.RUnlock()
	dbs := make([]*Database, 0, len(c.databases))
	for _, db := range c.databases {
		dbs = append(dbs, db)
	}
	return dbs
}

func (c *CacheManager) ListDatabaseNames() []string {
	c.mu.RLock()
	defer c.mu.RUnlock()
	names := make([]string, 0, len(c.databases))
	for name := range c.databases {
		names = append(names, name)
	}
	return names
}

func (c *CacheManager) Size() int {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return len(c.databases)
}

func (c *CacheManager) RemoveDatabase(name string) {
	c.mu.Lock()
	defer c.mu.Unlock()
	delete(c.databases, name)
}
