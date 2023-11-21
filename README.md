# RisegerDB - README
![image](https://github.com/MuLeiSY2021/RisegerDB/assets/92205763/855c98f5-3be1-4071-aa0a-f3110833f027)

**RisegerDB**是一个基于R树和R*树主索引的地理空间数据库系统，旨在通过高效的空间数据检索满足各类应用需求。本文档提供了RisegerDB的启动流程、示例用法以及支持的SQL命令语法概述。

这个项目的名字**RisegerDB**来源于 **“Rapid Search of Geographic Database”**（*Ri-se-ger-DB*）即“快速搜素地理数据库”，项目专为地理数据进行快速高效的搜索。
## RisegerDB

**[English](README-eng.md)**  

## 入门指南

要启动RisegerDB，请使用以下启动脚本：

```bash
./scripts/startup.sh
./scripts/shell.sh
```

## 示例用法

以下是使用RisegerDB的一系列命令示例：
```sql
get databases
```
![image](https://github.com/MuLeiSY2021/RisegerDB/assets/92205763/726efafa-93e0-4c51-abec-28e48ae57030)

```sql
preload './test/test_data.json'

use database 'test_db'

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


## SQL语法

### 搜索SQL

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

### 创建SQL

```bash
PRELOAD 'file_path'
```

### 使用语句

```bash
use_statement "|" use_statements
use_statement

use_statement:
  DATABASE 'database_name'
  | MAP 'map_name'
  | SCOPE rectangle_expression
  | MODEL 'model_expression'
```

### 搜索子句

```bash
search_clause: strings_expression
```

### WHERE子句

```bash
where_clause: bool_condition
```

### 布尔条件

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

## 实体定义

```bash
END:number
END:string
```

欢迎使用、修改和为RisegerDB贡献代码。有关更详细的信息和更新，请查看官方 [RisegerDB存储库](https://github.com/your_username/risegerdb).

愉快的编码！

string_entity -> string
| attribute_expression
END:number :> org.riseger.protoctl.compiler.function.Entity_f
END:string :> org.riseger.protoctl.compiler.function.Entity_f
