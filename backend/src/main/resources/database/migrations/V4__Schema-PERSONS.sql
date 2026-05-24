CREATE TABLE IF NOT EXISTS PERSONS(
    id UUID,
    user_id UUID NOT NULL,
    firstname VARCHAR(32) NOT NULL,
    surname VARCHAR(56) NOT NULL,
    email VARCHAR(25) NOT NULL,
    phone VARCHAR(15),
    city VARCHAR(42),
    street VARCHAR(82),
    post_code VARCHAR(6),
    building_number VARCHAR(5),
    building_floor VARCHAR(3),
    door_code VARCHAR(3),
    CONSTRAINT PERSONS_PK PRIMARY KEY (id),
    CONSTRAINT PERSONS_USERS_FK FOREIGN KEY (user_id) REFERENCES USERS(id)
);