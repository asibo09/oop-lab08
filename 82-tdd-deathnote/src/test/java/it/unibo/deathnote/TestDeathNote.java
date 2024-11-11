package it.unibo.deathnote;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImplementation;

import static it.unibo.deathnote.api.DeathNote.RULES;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestDeathNote {

    private DeathNote deathNote;
    private static final String LUIGI_FRANCO= "Luigi Franco";
    private static final String MARIO_MONTI= "Mario Monti";
    private static final int INVALID_CAUSE= 100;
    private static final int INVALID_DETAILS= 6000+INVALID_CAUSE;

    @BeforeEach
    void start(){
        deathNote= new DeathNoteImplementation();
    }

    @Test
    void testIllegalRules(){
        for (final var index : List.of(-1, RULES.size()+1)) {
            throw new IllegalArgumentException(deathNote.getRule(index));
        }
    }


    @Test
    void TestRules(){
        for (int i = 0; i <= RULES.size(); i++) {
            final var rule= deathNote.getRule(i);
            assertNotNull(RULES);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    void TestActualDeath(){
        assertFalse(deathNote.isNameWritten(LUIGI_FRANCO));
        deathNote.writeName(LUIGI_FRANCO);
        assertTrue(deathNote.isNameWritten(LUIGI_FRANCO));
        assertFalse(deathNote.isNameWritten(MARIO_MONTI));
        assertFalse(deathNote.isNameWritten(""));
    }

    @Test
    void TestDeathCause() throws InterruptedException{
        deathNote.writeDeathCause("spontaneus combustion");
        deathNote.writeName(MARIO_MONTI);
        assertEquals("heart attack", deathNote.getDeathCause(MARIO_MONTI));
        
        deathNote.writeName(LUIGI_FRANCO);
        assertEquals("karting accident", deathNote.getDeathCause(LUIGI_FRANCO));
        sleep(INVALID_CAUSE);

        assertFalse(deathNote.writeDeathCause("spontaneus combustion"));
        assertEquals("karting accident", deathNote.getDeathCause(LUIGI_FRANCO));
    } 


    @Test
    void TestDetailCause() throws InterruptedException{
        deathNote.writeDetails(MARIO_MONTI);
        deathNote.writeName(MARIO_MONTI);
        assertEquals("", deathNote.getDeathDetails(MARIO_MONTI));
        assertEquals("ran for too long", deathNote.getDeathDetails(MARIO_MONTI));

        deathNote.writeName(LUIGI_FRANCO);
        sleep(INVALID_DETAILS);

        assertFalse(deathNote.writeDeathCause("wrote too many test before dying"));
        assertEquals("", deathNote.getDeathCause(LUIGI_FRANCO));
    } 
}


/* 
package it.unibo.deathnote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImplementation;

import static it.unibo.deathnote.api.DeathNote.RULES;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TestDeathNote {

    private DeathNote deathNote;
    private static final String DANILO_PIANINI = "Danilo Pianini";
    private static final String LIGHT_YAGAMI = "Light Yagami";
    private static final int INVALID_CAUSE_TIME = 100;
    private static final int INVALID_DETAILS_TIME = 6000 + INVALID_CAUSE_TIME;

    @BeforeEach
    void init() {
        deathNote = new DeathNoteImplementation();
    }

    
    @Test
    void testIllegalRule() {
        for (final var index: List.of(-1, 0, RULES.size() + 1)) {
            assertThrows(
                new IllegalArgumentThrower() {
                    @Override
                    public void run() {
                        deathNote.getRule(index);
                    }
                }
            );
        }
    }

    
    @Test
    void testRules() {
        for (int i = 1; i <= RULES.size(); i++) {
            final var rule = deathNote.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

   
    @Test
    void testActualDeath() {
        assertFalse(deathNote.isNameWritten(DANILO_PIANINI));
        deathNote.writeName(DANILO_PIANINI);
        assertTrue(deathNote.isNameWritten(DANILO_PIANINI));
        assertFalse(deathNote.isNameWritten(LIGHT_YAGAMI));
        assertFalse(deathNote.isNameWritten(""));
    }

   
    @Test
    void testDeathCause() throws InterruptedException {
        assertThrows(
            new IllegalStateThrower() {
                @Override
                public void run() {
                    deathNote.writeDeathCause("spontaneous combustion");
                }
            }
        );
        deathNote.writeName(LIGHT_YAGAMI);
        assertEquals("heart attack", deathNote.getDeathCause(LIGHT_YAGAMI));
        deathNote.writeName(DANILO_PIANINI);
        assertTrue(deathNote.writeDeathCause("karting accident"));
        // Assuming the method can be executed in less than 40ms
        assertEquals("karting accident", deathNote.getDeathCause(DANILO_PIANINI));
        // Wait for more than 40 ms
        sleep(INVALID_CAUSE_TIME);
        assertFalse(deathNote.writeDeathCause("Spontaneous human combustion"));
        assertEquals("karting accident", deathNote.getDeathCause(DANILO_PIANINI));
    }

    
    @Test
    void testDeathDetails() throws InterruptedException {
        assertThrows(
            new IllegalStateThrower() {
                @Override
                public void run() {
                    deathNote.writeDetails(LIGHT_YAGAMI);
                }
            }
        );
        deathNote.writeName(LIGHT_YAGAMI);
        assertEquals("", deathNote.getDeathDetails(LIGHT_YAGAMI));
        assertTrue(deathNote.writeDetails("ran for too long"));
        // Assuming the method can be executed in less than 6040ms
        assertEquals("ran for too long", deathNote.getDeathDetails(LIGHT_YAGAMI));
        // Wait for more than 6040 ms
        deathNote.writeName(DANILO_PIANINI);
        sleep(INVALID_DETAILS_TIME);
        assertFalse(deathNote.writeDetails("wrote many tests before dying"));
        assertEquals("", deathNote.getDeathDetails(DANILO_PIANINI));
    }

    static void assertThrows(final RuntimeExceptionThrower exceptionThrower) {
        try {
            exceptionThrower.run();
            fail("Exception was expected, but not thrown");
        } catch (IllegalStateException | IllegalArgumentException e) {
            assertTrue(
                exceptionThrower instanceof IllegalArgumentThrower && e instanceof IllegalArgumentException // NOPMD
                || exceptionThrower instanceof IllegalStateThrower && e instanceof IllegalStateException // NOPMD
            );
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().isBlank());
        }
    }

    private interface RuntimeExceptionThrower {
        void run();
    }

    private interface IllegalStateThrower extends RuntimeExceptionThrower { }

    private interface IllegalArgumentThrower extends RuntimeExceptionThrower { }
}
*/