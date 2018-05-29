INSERT INTO roles (name) SELECT 'ROLE_USER'
                         WHERE
                           NOT EXISTS (
                               SELECT id FROM roles WHERE id = 1
                           );