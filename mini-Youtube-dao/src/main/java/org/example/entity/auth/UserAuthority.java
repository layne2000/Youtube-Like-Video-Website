package org.example.entity.auth;

import java.util.List;

public class UserAuthority {
    List<ElementOperationRoleAssociation> elementOperationRoleAssociationList;
    List<MenuRoleAssociation> menuRoleAssociationList;

    public List<ElementOperationRoleAssociation> getElementOperationRoleAssociationList() {
        return elementOperationRoleAssociationList;
    }

    public List<MenuRoleAssociation> getMenuRoleAssociationList() {
        return menuRoleAssociationList;
    }

    public void setElementOperationRoleAssociationList(List<ElementOperationRoleAssociation> elementOperationRoleAssociationList) {
        this.elementOperationRoleAssociationList = elementOperationRoleAssociationList;
    }

    public void setMenuRoleAssociationList(List<MenuRoleAssociation> menuRoleAssociationList) {
        this.menuRoleAssociationList = menuRoleAssociationList;
    }
}
