package com.cmdgames.rpg.domain.scenario.interactions;

import com.cmdgames.rpg.domain.characters.Enemy;

public final class Battle {

    private Enemy enemy;
    private static final int HIT = 1;
    private static final int RUN = 2;

    public Battle(final Enemy enemy){
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return this.enemy;
    }

    public String getEventMessage() {
        return String.format("Choose on the the actions: \n%d. HIT \n%d. RUN", HIT, RUN);
    }

    public BattleContext doPlayerAction(final BattleContext battleContext) {
        switch (battleContext.getCommandAction()){
            case HIT:
                this.enemy = BattleActions.doAttack(battleContext.getPlayer(), this.enemy);
                if(this.enemy.getHealth() == 0) {
                    BattleActions.getExperience(battleContext.getPlayer(), this.enemy);
                    battleContext.setFinished(true);
                    battleContext.setMessage("The enemy was defeated, you can now move forward" );
                }else {
                    battleContext.setMessage("The enemy was hit, his health is now " + this.enemy.getHealth());
                }
                break;
            case RUN:
                boolean canRun = BattleActions.canRun(battleContext.getPlayer(), this.enemy);
                if(canRun) {
                    battleContext.setRun(canRun);
                    battleContext.setMessage("You runned away from the battle");
                }
                else
                    battleContext.setMessage("You cannot run away from the battle");
                break;
        }
        return battleContext;
    }

    public BattleContext doEnemyAction(BattleContext battleContext) {
        if(this.enemy.getHealth() == 0){
            battleContext.setMessage("");
            return battleContext;
        }
        battleContext.setPlayer(
                BattleActions.getHit(battleContext.getPlayer(), this.enemy));
        if(battleContext.getPlayer().getHealth() <= 0) {
            battleContext.setMessage("You got a critical hit, your vision is fading away");
            battleContext.setDead(true);
        }else {
            battleContext.setMessage("You got hit, your health is now " + battleContext.getPlayer().getHealth());
        }
        return battleContext;
    }

}
