package cn.edu.hdu.ssd.common.ret;

import java.util.Set;

public class RolesAndPermsReturner
{
    private Set<String> rolesSet;
    private Set<String> permsSet;

    public RolesAndPermsReturner()
    {
    }

    public RolesAndPermsReturner(Set<String> rolesSet, Set<String> permsSet)
    {
        this.rolesSet = rolesSet;
        this.permsSet = permsSet;
    }

    public Set<String> getRolesSet()
    {
        return rolesSet;
    }

    public void setRolesSet(Set<String> rolesSet)
    {
        this.rolesSet = rolesSet;
    }

    public Set<String> getPermsSet()
    {
        return permsSet;
    }

    public void setPermsSet(Set<String> permsSet)
    {
        this.permsSet = permsSet;
    }
}
