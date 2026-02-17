package com.gildedrose;

/*
 * Atualiza itens Conjured: qualidade decai em dobro comparada ao item comum.
 */
class ConjuredItemUpdater implements ItemUpdater {
    @Override
    public boolean supports(Item item) {
        return item.name != null && item.name.startsWith("Conjured");
    }

    @Override
    public void update(Item item) {
        decreaseQuality(item, 2);
        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            decreaseQuality(item, 2);
        }
    }

    private void decreaseQuality(Item item, int amount) {
        item.quality = Math.max(0, item.quality - amount);
    }
}
