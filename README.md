# Alpha Wolf Mod

Transform into the red and black Alpha Wolf and lead the pack! This Minecraft mod lets you become the alpha predator, granting you extra hearts, damage resistance, and wolf-themed powers. Press a key to shift into a glowing red-and-black wolf form with particles and a special HUD overlay, perfect for players who want to dominate the wilderness with style.

## Install

1. Install the **Fabric Loader** for Minecraft 1.21.1 from [fabricmc.net](https://fabricmc.net/use/).
2. Install **Fabric API** for 1.21.1 into the `mods` folder of your Minecraft instance.
3. Copy the built `alpha-wolf-1.0.0.jar` from `build/libs` into the same `mods` folder.
4. Launch the game.

## Use

1. **Transform into the Alpha Wolf** by pressing **G** (configurable) or running `/alpha`.
2. **Enjoy wolf powers while transformed:**
   - Gain **6 extra hearts** (total max health increases to 16 hearts) and heal to full.
   - Take **50% less damage** from all sources.
   - Get **Night Vision** and a slight **speed boost**.
   - Emit **red flame** and **black smoke particles**.
   - Glow red to show your alpha status.
3. **Revert to human** by pressing **G** again or using `/alpha` again.

### Commands

- `/alpha` — Toggles the wolf transformation on or off.

### Keybind

- **G** (default) — Transform/Return. Change it in *Options → Controls → Key Binds → Alpha Wolf*.

### Configuration

The wolf powers are defined in `WolfManager.java`:
- `EXTRA_HEALTH = 12.0` — bonus max health (in half-hearts, so 12 = 6 hearts).
- `DAMAGE_REDUCTION = 0.5f` — fraction of incoming damage negated while a wolf (edit in `LivingEntityDamageMixin.java`).

### Notes

- Transformation state is per server session. Rejoining resets it (use `/alpha` again).
- The red glow and particles replace a full player-model change; the red-and-black theme is shown through glow, particles, and HUD overlay.

---

More Minecraft mod projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/76c334fd-50aa-4c7d-b5b2-16dd0e6dd2fc?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=76c334fd-50aa-4c7d-b5b2-16dd0e6dd2fc
