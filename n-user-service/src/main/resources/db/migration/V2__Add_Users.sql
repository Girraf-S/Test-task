insert into users (first_name,
                   last_name,
                   password,
                   email,
                   role,
                   enabled)
values ('Anakin',
        'Skywalker',
        '$2a$12$Y/jXrPZ/aD31VQopyhPcX..6y/GzU9nav6I4OMgAHeBWjIu5NwJma',
        'admin@gmail.com',
        'ADMIN',
        true),
       ('Bilbo',
        'Baggins',
        '$2a$12$SV3psaPIHKmI.b9QQv3g1u6l9inh6U.CzbTV2pO7.9iKBIyb6KkoO',
        'librarian@mail.com',
        'LIBRARIAN',
        true),
       ('Optimus',
        'Prime',
        '$2a$12$SV3psaPIHKmI.b9QQv3g1u6l9inh6U.CzbTV2pO7.9iKBIyb6KkoO',
        'reader@mail.com',
        'READER',
        true);
