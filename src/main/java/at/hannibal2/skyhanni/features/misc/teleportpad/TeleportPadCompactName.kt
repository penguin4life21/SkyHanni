package at.hannibal2.skyhanni.features.misc.teleportpad

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.SkyHanniRenderEntityEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.LorenzUtils.isInIsland
import at.hannibal2.skyhanni.utils.RegexUtils.matchMatcher
import at.hannibal2.skyhanni.utils.repopatterns.RepoPattern
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@SkyHanniModule
object TeleportPadCompactName {
    private val patternGroup = RepoPattern.group("misc.teleportpad")

    /**
     * REGEX-TEST: §a✦ §aWarp To §9Blue
     */
    private val namePattern by patternGroup.pattern(
        "name",
        "§.✦ §aWarp To (?<name>.*)"
    )

    /**
     * REGEX-TEST: §c✦ §cNo Destination
     */
    private val noNamePattern by patternGroup.pattern(
        "noname",
        "§.✦ §cNo Destination"
    )

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onRenderLiving(event: SkyHanniRenderEntityEvent.Specials.Pre<EntityLivingBase>) {
        if (!IslandType.PRIVATE_ISLAND.isInIsland()) return
        if (!SkyHanniMod.feature.misc.teleportPad.compactName) return
        val entity = event.entity
        if (entity !is EntityArmorStand) return

        val name = entity.name

        noNamePattern.matchMatcher(name) {
            event.cancel()
        }

        namePattern.matchMatcher(name) {
            entity.customNameTag = group("name")
        }
    }
}
