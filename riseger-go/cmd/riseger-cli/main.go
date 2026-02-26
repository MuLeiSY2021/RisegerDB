package main

import (
	"bufio"
	"encoding/json"
	"flag"
	"fmt"
	"net"
	"os"
	"strings"

	"github.com/riseger/riseger-go/pkg/protocol"
)

const banner = `
 ██████╗ ██╗███████╗███████╗ ██████╗ ███████╗██████╗ ██████╗ ██████╗
 ██╔══██╗██║██╔════╝██╔════╝██╔════╝ ██╔════╝██╔══██╗██╔══██╗██╔══██╗
 ██████╔╝██║███████╗█████╗  ██║  ███╗█████╗  ██████╔╝██║  ██║██████╔╝
 ██╔══██╗██║╚════██║██╔══╝  ██║   ██║██╔══╝  ██╔══██╗██║  ██║██╔══██╗
 ██║  ██║██║███████║███████╗╚██████╔╝███████╗██║  ██║██████╔╝██████╔╝
 ╚═╝  ╚═╝╚═╝╚══════╝╚══════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝╚═════╝ ╚═════╝
`

func main() {
	host := flag.String("host", "localhost", "server host")
	port := flag.Int("port", 12000, "server port")
	flag.Parse()

	addr := fmt.Sprintf("%s:%d", *host, *port)

	fmt.Print(banner)
	fmt.Printf("Connecting to %s...\n", addr)

	conn, err := net.Dial("tcp", addr)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Connection failed: %v\n", err)
		os.Exit(1)
	}
	defer conn.Close()

	fmt.Println("Connected! Type SQL queries ending with ';'. Type 'exit' to quit.")
	fmt.Println()

	scanner := bufio.NewScanner(os.Stdin)
	var queryBuf strings.Builder

	for {
		if queryBuf.Len() == 0 {
			fmt.Print("RisegerDB> ")
		} else {
			fmt.Print("       ... ")
		}

		if !scanner.Scan() {
			break
		}
		line := scanner.Text()

		if strings.TrimSpace(line) == "exit" || strings.TrimSpace(line) == "exit;" {
			fmt.Println("Bye!")
			return
		}

		queryBuf.WriteString(line)
		queryBuf.WriteString(" ")

		if !strings.HasSuffix(strings.TrimSpace(line), ";") {
			continue
		}

		query := strings.TrimSpace(queryBuf.String())
		query = strings.TrimSuffix(query, ";")
		query = strings.TrimSpace(query)
		queryBuf.Reset()

		if query == "" {
			continue
		}

		resp, err := sendQuery(conn, query)
		if err != nil {
			fmt.Fprintf(os.Stderr, "Error: %v\n", err)
			continue
		}

		printResponse(resp)
	}
}

func sendQuery(conn net.Conn, query string) (*protocol.Response, error) {
	req := protocol.Request{
		Type:  protocol.ReqShell,
		Query: query,
	}
	if err := protocol.WritePacket(conn, protocol.PacketTextSQL, &req); err != nil {
		return nil, err
	}

	_, data, err := protocol.ReadPacket(conn)
	if err != nil {
		return nil, err
	}

	var resp protocol.Response
	if err := json.Unmarshal(data, &resp); err != nil {
		return nil, err
	}
	return &resp, nil
}

func printResponse(resp *protocol.Response) {
	if !resp.Success {
		fmt.Printf("[ERROR] %s\n\n", resp.Error)
		return
	}

	if resp.RowCount == 0 {
		fmt.Println("Query OK, 0 rows affected.")
		fmt.Println()
		return
	}

	fmt.Printf("Query OK, %d rows.\n", resp.RowCount)

	if len(resp.Columns) == 0 && len(resp.Rows) > 0 {
		for k := range resp.Rows[0] {
			resp.Columns = append(resp.Columns, k)
		}
	}

	colWidths := make([]int, len(resp.Columns))
	for i, col := range resp.Columns {
		colWidths[i] = len(col)
	}
	for _, row := range resp.Rows {
		for i, col := range resp.Columns {
			val := fmt.Sprintf("%v", row[col])
			if len(val) > colWidths[i] {
				colWidths[i] = len(val)
			}
		}
	}

	printSeparator(colWidths)
	printRow(resp.Columns, colWidths)
	printSeparator(colWidths)
	for _, row := range resp.Rows {
		vals := make([]string, len(resp.Columns))
		for i, col := range resp.Columns {
			vals[i] = fmt.Sprintf("%v", row[col])
		}
		printRow(vals, colWidths)
	}
	printSeparator(colWidths)
	fmt.Println()
}

func printSeparator(widths []int) {
	fmt.Print("+")
	for _, w := range widths {
		fmt.Print(strings.Repeat("-", w+2))
		fmt.Print("+")
	}
	fmt.Println()
}

func printRow(values []string, widths []int) {
	fmt.Print("|")
	for i, val := range values {
		fmt.Printf(" %-*s |", widths[i], val)
	}
	fmt.Println()
}
