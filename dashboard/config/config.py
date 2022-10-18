import os

HOST = os.getenv('HOST', '0.0.0.0')
PORT = os.getenv('PORT', 8085)

DB_CONFIG = {
    'name': os.getenv('POSTGRES_DB', 'ghost_storage'),
    'user': os.getenv('POSTGRES_USER', 'postgres'),
    'password': os.getenv('POSTGRES_PASSWORD', 'Jad108fsdlknzc'),
    'host': os.getenv('POSTGRES_HOST', 'db'),
    'port': os.getenv('POSTGRES_PORT', '5432'),
}
