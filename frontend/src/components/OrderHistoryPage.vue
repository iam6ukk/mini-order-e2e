<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { getOrders } from "@/api/orderApi";

const router = useRouter();

const orders = ref([]);
const isLoading = ref(false);
const errorMessage = ref("");

const formatPrice = (price) => {
  return price.toLocaleString("ko-KR");
};

const loadOrders = async () => {
  isLoading.value = true;
  errorMessage.value = "";

  try {
    const response = await getOrders();
    orders.value = response.data;
  } catch (err) {
    console.error(err);
    errorMessage.value = "주문 내역을 불러오지 못했습니다.";
  } finally {
    isLoading.value = false;
  }
};

const moveToProducts = () => {
  router.push("/products");
};

onMounted(loadOrders);
</script>

<template>
  <main class="order-page">
    <header class="order-header">
      <h1>주문 내역</h1>
      <button type="button" class="secondary-button" @click="moveToProducts">상품 목록</button>
    </header>

    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

    <section v-if="isLoading" class="empty-state">주문 내역을 불러오는 중입니다.</section>

    <section v-else-if="orders.length === 0" class="empty-state">주문 내역이 없습니다.</section>

    <section v-else class="order-list" aria-label="주문 내역 목록">
      <article v-for="order in orders" :key="order.id" class="order-item">
        <header class="order-item-header">
          <div>
            <h2>주문 번호 {{ order.id }}</h2>
            <p>{{ order.status }}</p>
          </div>
          <strong>{{ formatPrice(order.totalPrice) }}원</strong>
        </header>

        <ul class="order-product-list">
          <li v-for="item in order.orderItems" :key="`${order.id}-${item.productName}`" class="order-product">
            <span>{{ item.productName }}</span>
            <small>{{ item.quantity }}개</small>
            <strong>{{ formatPrice(item.totalPrice) }}원</strong>
          </li>
        </ul>
      </article>
    </section>
  </main>
</template>

<style scoped>
.order-page {
  width: min(960px, calc(100% - 32px));
  margin: 40px auto;
  color: #1f2937;
}

.order-header,
.order-item-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.order-header {
  margin-bottom: 24px;
}

.order-header h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
}

.order-list {
  display: grid;
  gap: 12px;
}

.order-item {
  padding: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #ffffff;
}

.order-item-header {
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.order-item-header h2 {
  margin: 0 0 8px;
  font-size: 18px;
}

.order-item-header p {
  margin: 0;
  color: #6b7280;
}

.order-item-header strong {
  font-size: 18px;
}

.order-product-list {
  display: grid;
  gap: 10px;
  margin: 16px 0 0;
  padding: 0;
  list-style: none;
}

.order-product {
  display: grid;
  grid-template-columns: 1fr 80px 140px;
  align-items: center;
  gap: 16px;
}

.order-product small {
  color: #6b7280;
}

.order-product strong {
  justify-self: end;
}

.empty-state,
.error-message {
  padding: 32px;
  border-radius: 8px;
  text-align: center;
}

.empty-state {
  border: 1px dashed #d1d5db;
  color: #6b7280;
}

.error-message {
  margin-bottom: 16px;
  background: #fef2f2;
  color: #b91c1c;
}

button {
  min-height: 40px;
  border: 0;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 700;
}

.secondary-button {
  padding: 0 14px;
  background: #f3f4f6;
  color: #374151;
}

@media (max-width: 640px) {
  .order-header,
  .order-item-header {
    flex-direction: column;
    align-items: stretch;
  }

  .order-product {
    grid-template-columns: 1fr;
    gap: 6px;
  }

  .order-product strong {
    justify-self: start;
  }
}
</style>
