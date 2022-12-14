package com.example.madistudents.ui.faculty

class GroupOperator()
{
    private var groups: ArrayList<Group> = ArrayList()

    fun getGroups(): ArrayList<Group>
    {
        return groups
    }

    fun setGroups(newGroups: ArrayList<Group>)
    {
        groups = newGroups
    }
}