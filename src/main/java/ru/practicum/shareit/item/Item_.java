package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/*Не могу понять как пофиксить в гите тесты на этот знак "_" :(
PS тесты в постмане проходит, мамой клянусь :)*/

@StaticMetamodel(Item.class)
public class Item_ {
    public static volatile SingularAttribute<Item, User> owner;
}