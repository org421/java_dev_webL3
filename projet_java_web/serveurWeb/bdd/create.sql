DROP TABLE IF EXISTS mail_connexion;
DROP TABLE IF EXISTS ip_valide;
DROP TABLE IF EXISTS mail_confirmation;
DROP TABLE IF EXISTS evenement_document;
DROP TABLE IF EXISTS lien_document_user;
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS app_user_bo;
DROP TABLE IF EXISTS app_user;


CREATE TABLE app_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pseudo VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    mdp VARCHAR(255),
    is_valide BOOLEAN DEFAULT FALSE
);

CREATE TABLE app_user_bo (
     id INT AUTO_INCREMENT PRIMARY KEY,
     pseudo VARCHAR(255) UNIQUE,
     email VARCHAR(255) UNIQUE,
     mdp VARCHAR(255),
     role INT
);

CREATE TABLE document (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    id_user_proprio INT,
    FOREIGN KEY (id_user_proprio) REFERENCES app_user(id)
);

CREATE TABLE lien_document_user (
    id_document INT,
    id_user INT,
    role VARCHAR(255),
    derniere_connexion DATE,
    FOREIGN KEY (id_document) REFERENCES document(id),
    FOREIGN KEY (id_user) REFERENCES app_user(id)
);

CREATE TABLE evenement_document (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_document INT,
    date_debut DATE,
    date_fin DATE,
    nom VARCHAR(255),
    contenu VARCHAR(255),
    FOREIGN KEY (id_document) REFERENCES document(id)
);

CREATE TABLE mail_confirmation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    id_user INT,
    is_valide BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_user) REFERENCES app_user(id)
);

CREATE TABLE ip_valide (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ip VARCHAR(255),
    id_user INT,
    is_valide BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_user) REFERENCES app_user(id)
);

CREATE TABLE mail_connexion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    id_ip_valide INT,
    id_user INT,
    is_valide BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_ip_valide) REFERENCES ip_valide(id),
    FOREIGN KEY (id_user) REFERENCES app_user(id)
);

INSERT INTO app_user (pseudo, email, mdp) VALUES ('val', 'evrard2715@gmail.com','cdb6368db196d89af23d393951e2727eec76f2542a28ab9fefd2e71d66f191aa');
INSERT INTO app_user (pseudo, email, mdp) VALUES ('ozan', 'o@gmail.com','8f5173c20732fc63d1c944cd0972584431add13dfbd66e1d6103f7b4b6654d29');
INSERT INTO app_user_bo (pseudo, email, mdp,role) VALUES ('superAdmin', 'superAdmin@projetWeb.fr','f2abd764a48014df7bcdaf8617ebcd592a633629ee334691893660bc661e097d',1);
INSERT INTO document (nom, id_user_proprio) VALUES ('Planning Soutenance', 1);
INSERT INTO document (nom, id_user_proprio) VALUES ('Planning', 1);
INSERT INTO lien_document_user (id_document, id_user,role,derniere_connexion) VALUES (1, 1,'owner',null);
INSERT INTO lien_document_user (id_document, id_user,role,derniere_connexion) VALUES (1, 2,'editor',null);
