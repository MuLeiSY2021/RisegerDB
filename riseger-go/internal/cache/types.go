package cache

// Status represents the lifecycle state of a database.
type Status int

const (
	StatusLoading Status = iota
	StatusFailed
	StatusActive
)

func (s Status) String() string {
	switch s {
	case StatusLoading:
		return "LOADING"
	case StatusFailed:
		return "FAILED"
	case StatusActive:
		return "ACTIVE"
	default:
		return "UNKNOWN"
	}
}

// FieldType describes the type of a model parameter.
type FieldType int

const (
	FieldShort FieldType = iota
	FieldInt
	FieldLong
	FieldFloat
	FieldDouble
	FieldString
	FieldBool
	FieldCoord
	FieldLine
	FieldLoop
	FieldRect
)

func (t FieldType) String() string {
	names := [...]string{
		"SHORT", "INT", "LONG", "FLOAT", "DOUBLE",
		"STRING", "BOOLEAN", "COORD", "LINE", "LOOP", "RECT",
	}
	if int(t) < len(names) {
		return names[t]
	}
	return "UNKNOWN"
}

func ParseFieldType(s string) FieldType {
	switch s {
	case "SHORT":
		return FieldShort
	case "INT":
		return FieldInt
	case "LONG":
		return FieldLong
	case "FLOAT":
		return FieldFloat
	case "DOUBLE":
		return FieldDouble
	case "STRING":
		return FieldString
	case "BOOLEAN":
		return FieldBool
	case "COORD":
		return FieldCoord
	case "LINE":
		return FieldLine
	case "LOOP":
		return FieldLoop
	case "RECT":
		return FieldRect
	default:
		return FieldString
	}
}

// IsKey returns true if this field type is a spatial key (COORD, LINE, LOOP, RECT).
func (t FieldType) IsKey() bool {
	return t >= FieldCoord
}

// File/directory naming constants matching the Java Constant class.
const (
	SubmapPrefix   = "smp"
	MapPrefix      = "mp"
	ModelPrefix    = "mdl"
	DatabasePrefix = "db"
	LayerPrefix    = "layer"
	ConfigFileName = "config"
	ModelFileName  = "model"
	JSONSuffix     = "json"
	DotPrefix      = "."
)
