package geodata

import (
	"fmt"
	"io"
	"os"

	"github.com/riseger/riseger-go/pkg/geodata/pb"
	"google.golang.org/protobuf/proto"
)

// WriteFile 将 GeoDataFile 序列化为 protobuf 并写入 .geodata 文件。
func WriteFile(path string, data *pb.GeoDataFile) error {
	raw, err := proto.Marshal(data)
	if err != nil {
		return fmt.Errorf("marshal geodata: %w", err)
	}
	return os.WriteFile(path, raw, 0644)
}

// ReadFile 从 .geodata 文件读取并反序列化 GeoDataFile。
func ReadFile(path string) (*pb.GeoDataFile, error) {
	raw, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("read geodata file: %w", err)
	}
	var data pb.GeoDataFile
	if err := proto.Unmarshal(raw, &data); err != nil {
		return nil, fmt.Errorf("unmarshal geodata: %w", err)
	}
	return &data, nil
}

// WriteTo 序列化并写入任意 writer。
func WriteTo(w io.Writer, data *pb.GeoDataFile) error {
	raw, err := proto.Marshal(data)
	if err != nil {
		return fmt.Errorf("marshal geodata: %w", err)
	}
	_, err = w.Write(raw)
	return err
}
