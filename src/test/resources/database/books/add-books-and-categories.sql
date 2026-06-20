INSERT INTO categories (id, name, description, is_deleted)
VALUES (1, 'Fantasy', 'Fantasy books', false),
       (2, 'Science Fiction', 'Science fiction books', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'The Hobbit', 'J.R.R. Tolkien', '978-0-26110-2', 45.50, 'A great adventure', 'hobbit.jpg', false),
       (2, 'Dune', 'Frank Herbert', '978-0-44117-2', 55.00, 'Epic space opera', 'dune.jpg', false);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 2);