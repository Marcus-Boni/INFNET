package com.gildedrose;

/*
 * Atualiza itens comuns: qualidade cai 1 por dia e cai mais 1 após vencimento.
 */
class DefaultUpdater implements ItemUpdater {
    @Override
    public boolean supports(Item item) {
        return true;
    }

    @Override
    public void update(Item item) {
        decreaseQuality(item, 1);
        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            decreaseQuality(item, 1);
        }
    }

    private void decreaseQuality(Item item, int amount) {
        item.quality = Math.max(0, item.quality - amount);
    }
}
