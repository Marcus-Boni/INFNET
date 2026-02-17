package com.gildedrose;

/*
 * Atualiza Backstage Pass: qualidade aumenta conforme aproxima o evento e zera após vencimento.
 */
class BackstagePassUpdater implements ItemUpdater {
    private static final int MAX_QUALITY = 50;

    @Override
    public boolean supports(Item item) {
        return "Backstage passes to a TAFKAL80ETC concert".equals(item.name);
    }

    @Override
    public void update(Item item) {
        increaseQuality(item, 1);

        if (item.sellIn < 11) {
            increaseQuality(item, 1);
        }

        if (item.sellIn < 6) {
            increaseQuality(item, 1);
        }

        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            item.quality = 0;
        }
    }

    private void increaseQuality(Item item, int amount) {
        item.quality = Math.min(MAX_QUALITY, item.quality + amount);
    }
}
