import os


HOST = os.getenv('HOST', 'localhost')
PORT = os.getenv('DASHBOARD_PORT', 8085)

SPRING_PORT = os.getenv('PORT', 8080)
SPRING_DATA_URL = f'http://{HOST}:{SPRING_PORT}/document/'

DB_CONFIG = {
    'name': os.getenv('POSTGRES_DB', 'ghost_storage'),
    'user': os.getenv('POSTGRES_USER', 'postgres'),
    'password': os.getenv('POSTGRES_PASSWORD', 'Jad108fsdlknzc'),
    'host': os.getenv('POSTGRES_HOST', 'db'),
    'port': os.getenv('POSTGRES_PORT', '5432'),
}
