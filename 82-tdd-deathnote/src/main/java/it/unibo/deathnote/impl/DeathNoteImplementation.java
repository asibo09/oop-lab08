package it.unibo.deathnote.impl;

import it.unibo.deathnote.api.DeathNote;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DeathNoteImplementation implements DeathNote{

    private final Map<String,Death> death;
    private String lastName;
    
    public DeathNoteImplementation() {
        death = new LinkedHashMap<>();  // Predictable iteration order
    }

    @Override
    public String getRule(int ruleNumber) {
        if (ruleNumber<1 || ruleNumber> RULES.size()) {
            throw new IllegalArgumentException("Rules index "+ ruleNumber + " doesn't exist");
        }
        return RULES.get(ruleNumber-1);
    }

    @Override
    public void writeName(String name) {
        Objects.requireNonNull(name);
        lastName=name;
        death.put(name, new Death());
    }

    @Override
    public boolean writeDeathCause(String cause) {
        if (cause==null) {
            new DeathTransformer() {
                @Override
                public Death call(Death input) {
                    return input.writeCause(cause);
                }        
            };
            throw new IllegalArgumentException("No cause was written");
        }
        return false;
    }

    @Override
    public boolean writeDetails(String details) {
        if (details==null) {
            new DeathTransformer() {
                @Override
                public Death call(Death input) {
                    return input.writeDetails(details);
                }        
            };
            throw new IllegalArgumentException("No details was written");
        }
        return false;  
    }

    @Override
    public String getDeathCause(String name) {
        return getDeath(name).cause;
    }

    @Override
    public String getDeathDetails(String name) {
        return getDeath(name).details;
    }

    @Override
    public boolean isNameWritten(String name) {
       return death.containsKey(name);
    }

    private Death getDeath(final String name) {
        final var deaths = death.get(name);
        if (deaths == null) {
            throw new IllegalArgumentException(name + " has never been written in this notebook");
        }
        return deaths;
    }

    
}

interface DeathTransformer {
    Death call(Death input);
}

class Death{
    private static final byte TIMEOUT=40;
    private static final short SECOND_TIMEOUT=6000+TIMEOUT;
    final String cause;
    final String details;
    final long timeDeath;

    private Death(final String cause, final String details) {
        this.cause = cause;
        this.details = details;
        timeDeath = System.currentTimeMillis();
    }

    Death() {
        this("heart attack", "");
    }

    Death writeCause(String causeString){
        return System.currentTimeMillis() < timeDeath+TIMEOUT ? new Death(causeString,this.details) : this;
    }

    Death writeDetails(String details){
        return System.currentTimeMillis() < timeDeath+SECOND_TIMEOUT ? new Death(this.cause, details) : this;
    }




}