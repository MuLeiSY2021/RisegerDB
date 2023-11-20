启动函数:
./scripts/startup.sh
./scripts/shell.sh

例子:
get databases

preload './test/tset_data.json'

use databases 'test_db'

get maps

USE		
DATABASE 'test_db'|		
MAP 'china_mp'|		
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
coord(4000 ,1000),		
5000		
)		
OR OUT RECT(		
coord(777.5, 72658.1),		
45.2		
)		
AND building_model.floorArea > 1000;

语法：
sql -> search_sql
| create_sql

search_sql -> "USE" use_clause "SEARCH" search_clause "WHERE" where_clause
| "USE" use_clause "SEARCH" search_clause
| "SEARCH" search_clause "WHERE" where_clause
| "USE" use_clause
| "SEARCH" search_clause
| "WHERE" where_clause
| "GET" "DATABASES"
| "GET" "MAPS"
| "GET" "MODELS"

create_sql -> "PRELOAD"

use_statements -> use_statement "|" use_statements
| use_statement

use_statement -> "DATABASE" string
| "MAP" string
| "SCOPE" rectangle_expression
| "MODEL" strings_expression

search_clause -> strings_expression

where_clause -> bool_condition

bool_condition -> "(" bool_condition ")"
| bool_condition_0 "OR" bool_condition_0
| bool_condition_0

bool_condition_0 -> bool_condition_1 "AND" bool_condition_1
| bool_condition_1

bool_condition_1 -> "!" bool_condition_2
| bool_condition_2

bool_condition_2 -> "IN" graphic_expression
| "OUT" graphic_expression
| bool_condition_3

bool_condition_3 -> num_condition ">" bool_condition_3
| num_condition ">=" bool_condition_3
| num_condition "<" bool_condition_3
| num_condition "<=" bool_condition_3
| num_condition "=" bool_condition_3
| num_condition

num_condition -> "(" num_condition ")"
| "-" num_condition
| num_condition_0
num_condition_0
| num_condition_1 "+" num_condition
| num_condition_1 "-" num_condition
| num_condition_1

num_condition_1 -> number_entity "*" num_condition
| number_entity "/" num_condition
| number_entity

attribute_expression -> string "." string

graphic_expression -> rectangle_expression

rectangle_expression -> "RECT" "(" coord_expression "," num_condition ")"

coord_expression -> "COORD" "(" num_condition "," num_condition ")"

strings_expression -> string_entity "," strings_expression
| string_entity

number_entity -> number
| attribute_expression

string_entity -> string
| attribute_expression
END:number
END:string

string_entity -> string
| attribute_expression
END:number :> org.riseger.protoctl.compiler.function.Entity_f
END:string :> org.riseger.protoctl.compiler.function.Entity_f