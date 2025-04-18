package lobby.anni_bard_remake;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum BardEffectType {
    SPEED {
        @Override
        public void apply(Player target) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
        }
    },
    HASTE {
        @Override
        public void apply(Player target) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
        }
    },
    SLOW {
        @Override
        public void apply(Player target) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
        }
    },
    GLOWING {
        @Override
        public void apply(Player target) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));
        }
    },
    REGEN_HALF {
        @Override
        public void apply(Player target) {
            // 通常の再生1の回復量を再現するなら、本来は2.5秒ごとに1ハート（2HP）回復
            // ⇒ ここでは1.25秒ごとに1HP（0.5ハート）を回復する感じに
            // つまり回復量を半分に「見せかける」処理（擬似再生）

            // Customで直接回復（条件つきで）
            double healAmount = 1.0; // 半分の0.5ハート
            double newHealth = Math.min(target.getMaxHealth(), target.getHealth() + healAmount);
            target.setHealth(newHealth);

            // 視覚的に再生感を出すためにエフェクトも一応付けておく（ただし0.5秒ぐらい）
            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0));
        }
    };

    public abstract void apply(Player target);
}
