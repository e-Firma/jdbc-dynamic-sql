package org.jdbc.dynsql.test;

import java.sql.Date;

import org.jdbc.dynsql.test.Animal;

public class ZOO {

    private Date openFrom;
    private Date openTo;
    private Animal horse;
    private Animal dog;
    
    public Animal getHorse() {
        return horse;
    }
    public void setHorse(Animal horse) {
        this.horse = horse;
    }
    public Animal getDog() {
        return dog;
    }
    public void setDog(Animal dog) {
        this.dog = dog;
    }
    public Date getOpenFrom() {
        return openFrom;
    }
    public void setOpenFrom(Date openFrom) {
        this.openFrom = openFrom;
    }
    public Date getOpenTo() {
        return openTo;
    }
    public void setOpenTo(Date openTo) {
        this.openTo = openTo;
    }
    
}
