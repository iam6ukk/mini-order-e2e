<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { clearCartItems, deleteCartItem, getCartItems } from "../api/cartApi";
import { createOrder } from "../api/orderApi";

const router = useRouter();

const cartItems = ref([]);
const isLoading = ref(false);
const isOrdering = ref(false);
const errorMessage = ref("");

const totalPrice = computed(() => {
  return cartItems.value.reduce((sum, item) => {
    return sum + item.price * item.quantity;
  }, 0);
});

const isCartEmpty = computed(() => cartItems.value.length === 0);

const formatPrice = (price) => {
  return price.toLocaleString("ko-KR");
};

const loadCartItems = async () => {
  isLoading.value = true;
  errorMessage.value = "";

  try {
    const response = await getCartItems();
    cartItems.value = response.data;
  } catch (err) {
    console.error(err);
    errorMessage.value = "장바구니를 불러오지 못했습니다.";
  } finally {
    isLoading.value = false;
  }
};

const handleDeleteItem = async (cartItemId) => {
  errorMessage.value = "";

  try {
    await deleteCartItem(cartItemId);
    cartItems.value = cartItems.value.filter((item) => item.id !== cartItemId);
  } catch (err) {
    console.error(err);
    errorMessage.value = "상품을 삭제하지 못했습니다.";
  }
};

const handleClearItems = async () => {
  if (isCartEmpty.value) {
    return;
  }

  errorMessage.value = "";

  try {
    await clearCartItems();
    cartItems.value = [];
  } catch (err) {
    console.error(err);
    errorMessage.value = "장바구니를 비우지 못했습니다.";
  }
};

const handleCreateOrder = async () => {
  if (isCartEmpty.value || isOrdering.value) {
    return;
  }

  isOrdering.value = true;
  errorMessage.value = "";

  try {
    await createOrder();
    cartItems.value = [];
    router.push("/orders/complete");
  } catch (err) {
    console.error(err);
    errorMessage.value = err.response.data.message;
  } finally {
    isOrdering.value = false;
  }
};

const moveToProducts = () => {
  router.push("/products");
};

onMounted(loadCartItems);
</script>

<template>
  <main class="cart-page">
    <header class="cart-header">
      <h1>장바구니</h1>
      <div class="header-actions">
        <button type="button" class="secondary-button" @click="moveToProducts">상품 목록</button>
        <button type="button" class="secondary-button" :disabled="isCartEmpty" @click="handleClearItems">전체 비우기</button>
      </div>
    </header>

    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

    <section v-if="isLoading" class="empty-state">장바구니를 불러오는 중입니다.</section>

    <section v-else-if="isCartEmpty" class="empty-state">장바구니가 비어 있습니다.</section>

    <section v-else class="cart-list" aria-label="장바구니 상품 목록">
      <article v-for="item in cartItems" :key="item.id" class="cart-item">
        <div class="item-info">
          <h2>{{ item.productName }}</h2>
          <p>수량 {{ item.quantity }}개</p>
        </div>

        <div class="item-price">
          <span>{{ formatPrice(item.price * item.quantity) }}원</span>
          <small>개당 {{ formatPrice(item.price) }}원</small>
        </div>

        <button type="button" class="delete-button" @click="handleDeleteItem(item.id)">삭제</button>
      </article>
    </section>

    <footer class="cart-summary">
      <div>
        <span>총 금액</span>
        <strong>{{ formatPrice(totalPrice) }}원</strong>
      </div>
      <button type="button" class="order-button" :disabled="isCartEmpty || isOrdering" @click="handleCreateOrder">
        {{ isOrdering ? "주문 중..." : "주문하기" }}
      </button>
    </footer>
  </main>
</template>

<style scoped>
.cart-page {
  width: min(960px, calc(100% - 32px));
  margin: 40px auto;
  color: #1f2937;
}

.cart-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
}

.cart-header h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.cart-list {
  display: grid;
  gap: 12px;
}

.cart-item {
  display: grid;
  grid-template-columns: 1fr 180px 80px;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #ffffff;
}

.item-info h2 {
  margin: 0 0 8px;
  font-size: 18px;
}

.item-info p,
.item-price small {
  margin: 0;
  color: #6b7280;
}

.item-price {
  display: grid;
  justify-items: end;
  gap: 4px;
}

.item-price span {
  font-weight: 700;
}

.cart-summary {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-top: 24px;
  padding: 24px;
  border-top: 2px solid #111827;
}

.cart-summary div {
  display: grid;
  gap: 6px;
}

.cart-summary span {
  color: #6b7280;
}

.cart-summary strong {
  font-size: 28px;
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

button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.secondary-button,
.delete-button {
  padding: 0 14px;
  background: #f3f4f6;
  color: #374151;
}

.order-button {
  min-width: 160px;
  padding: 0 24px;
  background: #111827;
  color: #ffffff;
}

@media (max-width: 640px) {
  .cart-header,
  .cart-summary,
  .cart-item {
    align-items: stretch;
  }

  .cart-header,
  .cart-summary {
    flex-direction: column;
  }

  .header-actions {
    display: grid;
    width: 100%;
  }

  .cart-item {
    grid-template-columns: 1fr;
  }

  .item-price {
    justify-items: start;
  }
}
</style>
