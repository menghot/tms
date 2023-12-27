docker run -d -p 9208:9200 -p 9308:9300 -e "xpack.security.enabled=false" -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:8.10.2


# Postgres Environment Variables
DB_DRIVER_CLASS='org.postgresql.Driver'
DB_SCHEME='postgresql'
DB_PARAMS='allowPublicKeyRetrieval=true&useSSL=true&serverTimezone=UTC'
DB_USER='<YOUR_POSTGRES_USER_NAME>'
DB_USER_PASSWORD='<YOUR_POSTGRES_USER_PASSWORD>'
DB_HOST='<YOUR_POSTGRES_HOST_NAME>'
DB_PORT='<YOUR_POSTGRES_PORT>'
OM_DATABASE='<YOUR_POSTGRES_DATABASE_NAME>'

ELASTICSEARCH_SOCKET_TIMEOUT_SECS='60'
ELASTICSEARCH_USER='<ES_USERNAME>'
ELASTICSEARCH_CONNECTION_TIMEOUT_SECS='5'
ELASTICSEARCH_PORT='443'
ELASTICSEARCH_SCHEME='https'
ELASTICSEARCH_BATCH_SIZE='10'
ELASTICSEARCH_HOST='vpc-<random_characters>.<aws_region>.es.amazonaws.com'
ELASTICSEARCH_PASSWORD='<ES_PASSWORD>'
curl -XPOST http://localhost:8080/api/v1/openmetadata/enable --data-raw '{"dag_id": "example_bash_operator"}' -u "admin:admin" --header 'Content-Type: application/json'

