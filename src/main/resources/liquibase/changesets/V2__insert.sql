insert into users(name, username, email, password)
values ('kirill', 'isthatkirill', 'isthatkirill@yandex.ru', '$2a$10$XuXAvrwHuN1zGvG/cuUZI.0W60KuJgFJ9JpWqyJ2tFCd/MEb5QtT6'),
       ('anna', 'annushka', 'annushka@yandex.ru', '$2a$10$Tk/USu2.k47J.O6ynxXNSO2I5EhvdTYN8IuGhRRLyE0zH02qmf5k2');

insert into user_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');