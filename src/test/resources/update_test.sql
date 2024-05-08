USE
    DATABASE 'test_db'|
  MAP 'china_mp'|
  SCOPE RECT(
    [1, 2],
    20000
  )|
  MODEL province_scope.area_scope.building_model
UPDATE
    building.name = 'Hahaha',
WHERE
    IN RECT(
    [4000
    , 1000]
    , 5000
    )
   OR OUT RECT(
    [777.5
    , 72658.1]
    , 45.2
    )
  AND building_model.floorArea
    > 1000;