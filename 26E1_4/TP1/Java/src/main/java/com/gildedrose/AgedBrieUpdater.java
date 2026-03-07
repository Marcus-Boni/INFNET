package com.gildedrose;

/*
 * Atualiza Aged Brie: qualidade aumenta com o tempo e dobra após vencimento,
 * respeitando limite máximo de 50.
 */
class AgedBrieUpdater implements ItemUpdater {
    private static final int MAX_QUALITY = 50;

    @Override
    public boolean supports(Item item) {
        return "Aged Brie".equals(item.name);
    }

    @Override
    public void update(Item item) {
        increaseQuality(item, 1);
        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            increaseQuality(item, 1);
        }
    }

    private void increaseQuality(Item item, int amount) {
        item.quality = Math.min(MAX_QUALITY, item.quality + amount);
    }
}
