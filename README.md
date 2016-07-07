
# API

POST /scrape - save new scrape entry
POST /scrape/test - test scrape entry before saving
GET /scrape/<id> - scrape and retrieve data

POST /monitor/<id> - create a new monitor for entry


Examples:
curl -XPOST /save -d '{
"url": "http://www.realclearpolitics.com/epolls/latest_polls/", 
"target": "div#table-1,table@1,tbody,tr*,td@2",
"dtype": "string",
"name": ""}

curl -XPOST /test -d '{
"url": "http://www.realclearpolitics.com/epolls/latest_polls/", 
"target": "div#table-1,table@1,tbody,tr*,td@2",
"dtype": "string"
}

curl -XPOST /action/case/<id> -d '{
"name": "Some name",
"when": {"gt": 20},
"then": {"notify-email": 10}
}'

curl -XPOST /action/case/<id> -d '{
"name": "Some name",
"when": {"contains": "hello world"},
"then": {"notify-email": 10}
}'

curl -XPOST /action/store/<id> -d '{
"interval": {"minute": 15}
"action": {"id": 11}
}'

TABLES

Entries
_______
id INTEGER,
user_id INTEGER,
name VARCHAR(128),
target VARCHAR(512),
target_dtype VARCHAR(30),
created_on INTEGER

Actions
id INTEGER,
name VARCHAR(128),
type VARCHAR(30),



