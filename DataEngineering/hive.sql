
--создание таблицы
create external table if not exists default.crypto_table (
symbol string,
price float)
PARTITIONED BY(sampling_datetime timestamp)
location '/user/team02/hive/';


--оконные функции
select  *,
        MAX(price) OVER(PARTITION BY symbol ORDER BY sampling_datetime) as max_price, 
        MIN(price) OVER(PARTITION BY symbol ORDER BY sampling_datetime) as min_price
from crypto_table;