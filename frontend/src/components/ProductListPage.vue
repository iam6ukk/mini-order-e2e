<script setup>
import { ref, onMounted } from "vue";
import { getProducts } from "../api/productApi";

const products = ref([]);

onMounted(async () => {
  try {
    const response = await getProducts();
    products.value = response.data;
  } catch (err) {
    console.error(err);
  }
});
</script>

<template>
  <main>
    <h1>상품 목록</h1>
    <section v-for="product in products" :key="product.id">
      <h2>{{ product.name }}</h2>
      <p>가격: {{ product.price.toLocaleString() }}원</p>
      <p>재고: {{ product.stockQuantity }}개</p>
      <button>장바구니 담기</button>
    </section>
  </main>
</template>
