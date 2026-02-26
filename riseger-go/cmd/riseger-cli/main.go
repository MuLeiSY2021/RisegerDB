package main

import (
	"bufio"
	"bytes"
	"encoding/json"
	"flag"
	"fmt"
	"net/http"
	"os"
	"strings"
)

const banner = `
 ██████╗ ██╗███████╗███████╗ ██████╗ ███████╗██████╗ ██████╗ ██████╗
 ██╔══██╗██║██╔════╝██╔════╝██╔════╝ ██╔════╝██╔══██╗██╔══██╗██╔══██╗
 ██████╔╝██║███████╗█████╗  ██║  ███╗█████╗  ██████╔╝██║  ██║██████╔╝
 ██╔══██╗██║╚════██║██╔══╝  ██║   ██║██╔══╝  ██╔══██╗██║  ██║██╔══██╗
 ██║  ██║██║███████║███████╗╚██████╔╝███████╗██║  ██║██████╔╝██████╔╝
 ╚═╝  ╚═╝╚═╝╚══════╝╚══════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝╚═════╝ ╚═════╝
`

type queryRequest struct {
	SQL string `json:"sql"`
}

type queryResponse struct {
	Success  bool                     `json:"success"`
	Error    string                   `json:"error,omitempty"`
	Columns  []string                 `json:"columns,omitempty"`
	Rows     []map[string]interface{} `json:"rows,omitempty"`
	RowCount int                      `json:"rowCount"`
	Time     string                   `json:"time,omitempty"`
}

func main() {
	host := flag.String("host", "localhost", "server host")
	port := flag.Int("port", 12000, "server port")
	tls := flag.Bool("https", false, "use HTTPS")
	flag.Parse()

	scheme := "http"
	if *tls {
		scheme = "https"
	}
	baseURL := fmt.Sprintf("%s://%s:%d", scheme, *host, *port)

	fmt.Print(banner)
	fmt.Printf("Connecting to %s ...\n", baseURL)

	resp, err := http.Get(baseURL + "/health")
	if err != nil {
		fmt.Fprintf(os.Stderr, "Cannot connect to server: %v\n", err)
		os.Exit(1)
	}
	resp.Body.Close()

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

		qr, err := sendQuery(baseURL, query)
		if err != nil {
			fmt.Fprintf(os.Stderr, "Error: %v\n", err)
			continue
		}

		printResponse(qr)
	}
}

func sendQuery(baseURL, sql string) (*queryResponse, error) {
	body, _ := json.Marshal(queryRequest{SQL: sql})
	resp, err := http.Post(baseURL+"/query", "application/json", bytes.NewReader(body))
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var qr queryResponse
	if err := json.NewDecoder(resp.Body).Decode(&qr); err != nil {
		return nil, err
	}
	return &qr, nil
}

func printResponse(resp *queryResponse) {
	if !resp.Success {
		fmt.Printf("[ERROR] %s\n\n", resp.Error)
		return
	}

	if resp.RowCount == 0 {
		fmt.Printf("Query OK, 0 rows affected. (%s)\n\n", resp.Time)
		return
	}

	fmt.Printf("Query OK, %d rows. (%s)\n", resp.RowCount, resp.Time)

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
