# RisegerDB - README
![image](https://github.com/MuLeiSY2021/RisegerDB/assets/92205763/40efff70-f058-4f94-a372-3f431dbd8da4)

RisegerDB is an open-source spatial database system designed for efficient spatial data retrieval using R-trees and R*-trees as the primary indexes. This document provides an overview of the startup procedures, example usage, and the syntax for SQL commands supported by RisegerDB.

## Getting Started

To start RisegerDB, use the following startup scripts:

```bash
./scripts/startup.sh
./scripts/shell.sh
```

## Example Usage

Here is an example sequence of commands using RisegerDB:
```sql
get databases
```
![image](https://github.com/MuLeiSY2021/RisegerDB/assets/92205763/726efafa-93e0-4c51-abec-28e48ae57030)

```sql
preload './test/test_data.json'

use databases 'test_db'

get maps
```
![image](https://github.com/MuLeiSY2021/RisegerDB/assets/92205763/2024c928-708f-457d-8f1f-d498c841953a)

```sql
USE		
  DATABASE 'test_db'|		
  MAP 'china_map'|		
  SCOPE RECT(		
    COORD(1, 2),		
    20000		
  )|		
  MODEL 'province_scope.area_scope.building_model'		
SEARCH		
  'building_model.KEY_LOOP',		
  'building_model.name'		
WHERE		
  IN RECT(		
    COORD(4000, 1000),		
    5000		
  )		
  OR OUT RECT(		
    COORD(777.5, 72658.1),		
    45.2		
  )		
  AND building_model.floorArea > 1000;
```

![image](https://github.com/MuLeiSY2021/RisegerDB/assets/92205763/e3897021-81fd-49ca-a585-72e1bf05fea8)


## SQL Syntax

### Search SQL

```bash
USE use_clause SEARCH search_clause WHERE where_clause
USE use_clause SEARCH search_clause
SEARCH search_clause WHERE where_clause
USE use_clause
SEARCH search_clause
WHERE where_clause
GET DATABASES
GET MAPS
GET MODELS
```

### Create SQL

```bash
PRELOAD 'file_path'
```

### Use Statements

```bash
use_statement "|" use_statements
use_statement

use_statement:
  DATABASE 'database_name'
  | MAP 'map_name'
  | SCOPE rectangle_expression
  | MODEL 'model_expression'
```

### Search Clause

```bash
search_clause: strings_expression
```

### Where Clause

```bash
where_clause: bool_condition
```

### Boolean Conditions

```bash
bool_condition:
  "(" bool_condition ")"
  | bool_condition_0 "OR" bool_condition_0
  | bool_condition_0

bool_condition_0:
  bool_condition_1 "AND" bool_condition_1
  | bool_condition_1

bool_condition_1:
  "!" bool_condition_2
  | bool_condition_2

bool_condition_2:
  "IN" graphic_expression
  | "OUT" graphic_expression
  | bool_condition_3

bool_condition_3:
  num_condition ">" bool_condition_3
  | num_condition ">=" bool_condition_3
  | num_condition "<" bool_condition_3
  | num_condition "<=" bool_condition_3
  | num_condition "=" bool_condition_3
  | num_condition

num_condition:
  "(" num_condition ")"
  | "-" num_condition
  | num_condition_0
num_condition_0:
  | num_condition_1 "+" num_condition
  | num_condition_1 "-" num_condition
  | num_condition_1

num_condition_1:
  number_entity "*" num_condition
  | number_entity "/" num_condition
  | number_entity

attribute_expression:
  string "." string

graphic_expression:
  rectangle_expression

rectangle_expression:
  "RECT" "(" coord_expression "," num_condition ")"

coord_expression:
  "COORD" "(" num_condition "," num_condition ")"

strings_expression:
  string_entity "," strings_expression
  | string_entity

number_entity:
  number
  | attribute_expression

string_entity:
  string
  | attribute_expression
```

## Entity Definitions

```bash
END:number
END:string
```

Feel free to use, modify, and contribute to RisegerDB. For more detailed information and updates, check the official [RisegerDB repository](https://github.com/your_username/risegerdb).

Happy coding!
END:string

string_entity -> string
| attribute_expression
END:number :> org.riseger.protoctl.compiler.function.Entity_f
END:string :> org.riseger.protoctl.compiler.function.Entity_f
