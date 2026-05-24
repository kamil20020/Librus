INSERT INTO ROLES VALUES
    ('85838711-4398-4879-82c9-abacf5b5d9b5', 'ADMIN'),
    ('fad866ad-ec99-4151-b3ed-5c11046bac82', 'STUDENT'),
    ('d7ebff0a-86e3-4ae9-9b89-d4edd859017a', 'TEACHER'),
    ('1e72b454-fdc8-4ca6-ae25-b4fff735bdf6', 'PARENT');

INSERT INTO USERS VALUES
    ('eea83dca-6f04-41e9-bfe4-cbd659f9fbc4', 'admin', '$2a$10$s/tuJ.Z9KpkGYoueeT4PLeraptB0RsfF1yLss4.GvsjoX0bq5vt2O', 'admin@mail.com', '+48 123 456 789'),
    ('83f8d8d2-0e54-4bc7-9d2f-f959ddf48fbd', 'student', '$2a$10$inXbdjTqCX/40UAmUTqVWOggJs6dv/v2YGgBFJFQUSEwvhzlPMoxu', 'student@mail.com', '+42 789 654 123'),
    ('5e1e1d15-550e-4469-911e-6dcc6480f37d', 'teacher', '$2a$10$4f9c9YhBJPQPsDbVpMBbI.1S3TaR8x09YjU1eJuxJ.jN27wzhkv.K', 'teacher@mail.com', '+47 987 654 321'),
    ('73fcecac-0042-4ac7-a6b5-91023b688cb1', 'parent', '$2a$10$XrLN5PW5fWnRM62pMvWTD.A3imTvmGju49Sn/lsRsmq9sXF5hlepK', 'parent@mail.com', '+47 456 789 123');

INSERT INTO USERS_ROLES VALUES
   ('eea83dca-6f04-41e9-bfe4-cbd659f9fbc4', '85838711-4398-4879-82c9-abacf5b5d9b5'),
   ('83f8d8d2-0e54-4bc7-9d2f-f959ddf48fbd', 'fad866ad-ec99-4151-b3ed-5c11046bac82'),
   ('5e1e1d15-550e-4469-911e-6dcc6480f37d', 'd7ebff0a-86e3-4ae9-9b89-d4edd859017a'),
   ('73fcecac-0042-4ac7-a6b5-91023b688cb1', '1e72b454-fdc8-4ca6-ae25-b4fff735bdf6');

INSERT INTO PERSONS VALUES
   ('dde3f0fc-92d8-495b-84a0-2b43bed599dc', 'eea83dca-6f04-41e9-bfe4-cbd659f9fbc4', 'Adam', 'Kowalski', 'admin@mail.com', '+48 123 456 789', 'Wrocław', 'Ulica 123', '12-345', '1A', '2', '6'),
   ('46876620-5ef1-42fd-9fe5-c1c5d31b6e54', '83f8d8d2-0e54-4bc7-9d2f-f959ddf48fbd', 'Michał', 'Nowak', 'student@mail.com', '+42 789 654 123', 'Wrocław', 'Ulica 321', '13-346', '2B', '3', '8'),
   ('d854944d-5bd8-4732-ab86-71691a9889b5', '5e1e1d15-550e-4469-911e-6dcc6480f37d', 'Jan', 'Kowalski', 'teacher@mail.com', '+47 987 654 321', 'Wrocław', 'Ulica 132', '14-347', '3', '1', '3'),
   ('0f9fa49b-28fa-4d48-87aa-c9b0ce56a7ed', '73fcecac-0042-4ac7-a6b5-91023b688cb1', 'Jan', 'Nowak', 'parent@mail.com', '+47 456 789 123', 'Wrocław', 'Ulica 321', '13-346', '2B', '3', '8');
