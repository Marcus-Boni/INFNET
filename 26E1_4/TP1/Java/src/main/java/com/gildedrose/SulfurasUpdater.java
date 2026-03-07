package com.gildedrose;

/*
 * Atualiza Sulfuras: item lendário, não altera qualidade nem sellIn.
 */
class SulfurasUpdater implements ItemUpdater {
    @Override
    public boolean supports(Item item) {
        return "Sulfuras, Hand of Ragnaros".equals(item.name);
    }

    @Override
    public void update(Item item) {
        // Sulfuras não sofre alterações.
    }
}
