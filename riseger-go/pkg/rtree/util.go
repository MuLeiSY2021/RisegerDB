package rtree

import "strconv"

func floatStr(f float64) string {
	return strconv.FormatFloat(f, 'f', -1, 64)
}

func toRectSlice[T Rectangle](s []T) []Rectangle {
	out := make([]Rectangle, len(s))
	for i, v := range s {
		out[i] = v
	}
	return out
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}
