--
-- File generated with SQLiteStudio v3.4.4 on Sat Dec 23 13:32:20 2023
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: resep
CREATE TABLE IF NOT EXISTS resep (
    id_resep   INTEGER PRIMARY KEY AUTOINCREMENT,
    id_user,
    nama_resep TEXT,
    deskrripsi TEXT,
    foto_resep TEXT
);

INSERT INTO resep (
                      id_resep,
                      id_user,
                      nama_resep,
                      deskrripsi,
                      foto_resep
                  )
                  VALUES (
                      1,
                      2,
                      'Telur Goreng',
                      'Goreng aja telurnya',
                      NULL
                  );


-- Table: user
CREATE TABLE IF NOT EXISTS user (
    id_user    INTEGER PRIMARY KEY AUTOINCREMENT,
    email_user,
    pass_user  TEXT,
    nama_user  TEXT,
    foto_user  TEXT
);

INSERT INTO user (
                     id_user,
                     email_user,
                     pass_user,
                     nama_user,
                     foto_user
                 )
                 VALUES (
                     1,
                     'arul@gmail.com',
                     'arul',
                     'Syahrul',
                     NULL
                 );

INSERT INTO user (
                     id_user,
                     email_user,
                     pass_user,
                     nama_user,
                     foto_user
                 )
                 VALUES (
                     2,
                     'almas@gmail.com',
                     'almas',
                     'Almas',
                     NULL
                 );

INSERT INTO user (
                     id_user,
                     email_user,
                     pass_user,
                     nama_user,
                     foto_user
                 )
                 VALUES (
                     3,
                     'ayra@gmail.com',
                     'ayra',
                     'Ayra',
                     NULL
                 );


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
