package lobby.anni_bard_remake;

import org.bukkit.Sound;

public enum BardEffect {

    REGEN_BUFF(
            true,
            BardEffectType.REGEN_HALF,
            Sound.MUSIC_DISC_MALL,
            20
    ),
    SPEED_BUFF(
            true,  // 味方に付与
            BardEffectType.SPEED,
            Sound.MUSIC_DISC_FAR,
            20  // 範囲（ブロック数）
    ),
    HASTE(
            true,
            BardEffectType.HASTE,
            Sound.MUSIC_DISC_STRAD,
            20
    ),
    SLOW(
            false,
            BardEffectType.SLOW,
            Sound.MUSIC_DISC_MELLOHI,
            20
    ),
    GLOWING(
            false,
            BardEffectType.GLOWING,
            Sound.MUSIC_DISC_STAL,
            20
    );

    private final boolean friendly;  // true: 味方, false: 敵
    private final BardEffectType effect;
    private final Sound bgm;
    private final int range;

    BardEffect(boolean friendly, BardEffectType effect, Sound bgm  , int range) {
        this.friendly = friendly;
        this.effect = effect;
        this.bgm = bgm;
        this.range = range;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public BardEffectType getEffect() {
        return effect;
    }

    public Sound getBgm() {
        return bgm;
    }

    public int getRange() {
        return range;
    }
}

