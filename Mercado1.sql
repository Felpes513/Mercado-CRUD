CREATE database Mercado;

use mercado;

create table Produtos(
Id bigint auto_increment primary key,
Nome varchar (100),
Preco decimal(10,2),
Quantidade integer(10) not null
);


Show databases;

Select * From Produtos;

Drop table Produtos;

delete FROM Produtos where id = ?;