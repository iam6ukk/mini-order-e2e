<script setup>
import { onBeforeUnmount, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { addCartItem } from "../api/cartApi";
import { getProducts } from "../api/productApi";

const router = useRouter();

const products = ref([]);
const selectedQuantities = ref({});
const errorMessage = ref("");
const successMessage = ref("");
const addingProductId = ref(null);
let toastTimer = null;

const formatPrice = (price) => {
  return price.toLocaleString("ko-KR");
};

const getSelectedQuantity = (productId) => {
  return selectedQuantities.value[productId] ?? 0;
};

const increaseQuantity = (product) => {
  const currentQuantity = getSelectedQuantity(product.id);

  if (currentQuantity >= product.stockQuantity) {
    return;
  }

  selectedQuantities.value = {
    ...selectedQuantities.value,
    [product.id]: currentQuantity + 1,
  };
};

const decreaseQuantity = (product) => {
  const currentQuantity = getSelectedQuantity(product.id);

  if (currentQuantity <= 0) {
    return;
  }

  selectedQuantities.value = {
    ...selectedQuantities.value,
    [product.id]: currentQuantity - 1,
  };
};

const clearToastTimer = () => {
  if (toastTimer) {
    clearTimeout(toastTimer);
    toastTimer = null;
  }
};

const showToast = (type, message) => {
  clearToastTimer();

  if (type === "success") {
    successMessage.value = message;
    errorMessage.value = "";
  } else {
    errorMessage.value = message;
    successMessage.value = "";
  }

  toastTimer = setTimeout(() => {
    successMessage.value = "";
    errorMessage.value = "";
    toastTimer = null;
  }, 2000);
};

onMounted(async () => {
  try {
    const response = await getProducts();
    products.value = response.data;
    selectedQuantities.value = response.data.reduce((quantities, product) => {
      quantities[product.id] = 0;
      return quantities;
    }, {});
  } catch (err) {
    console.error(err);
    showToast("error", "상품 목록을 불러오지 못했습니다.");
  }
});

const handleAddCart = async (product) => {
  const selectedQuantity = getSelectedQuantity(product.id);
  const quantity = selectedQuantity === 0 ? 1 : selectedQuantity;

  if (product.stockQuantity <= 0 || quantity > product.stockQuantity) {
    return;
  }

  addingProductId.value = product.id;
  errorMessage.value = "";
  successMessage.value = "";

  try {
    await addCartItem(product.id, quantity);
    selectedQuantities.value = {
      ...selectedQuantities.value,
      [product.id]: 0,
    };
    showToast("success", "장바구니에 상품이 추가되었습니다.");
  } catch (err) {
    console.error(err);
    showToast("error", "장바구니에 상품을 담지 못했습니다.");
  } finally {
    addingProductId.value = null;
  }
};

const moveToCart = () => {
  router.push("/cart");
};

onBeforeUnmount(clearToastTimer);
</script>

<template>
  <main class="product-page">
    <header class="product-header">
      <h1>상품 목록</h1>
      <button type="button" class="secondary-button" @click="moveToCart">장바구니 이동</button>
    </header>

    <div v-if="errorMessage || successMessage" class="toast" :class="{ 'toast-error': errorMessage }" role="status">
      {{ successMessage || errorMessage }}
    </div>

    <section v-if="products.length === 0" class="empty-state">등록된 상품이 없습니다.</section>

    <section v-else class="product-list" aria-label="상품 목록">
      <article v-for="product in products" :key="product.id" class="product-item">
        <div class="product-info">
          <h2>{{ product.name }}</h2>
          <p>재고 {{ product.stockQuantity }}개</p>
        </div>

        <div class="product-price">
          <span>{{ formatPrice(product.price) }}원</span>
          <small>선택 {{ getSelectedQuantity(product.id) }}개</small>
        </div>

        <div class="quantity-control" aria-label="상품 수량 선택">
          <button type="button" :disabled="getSelectedQuantity(product.id) <= 0" @click="decreaseQuantity(product)">-</button>
          <strong>{{ getSelectedQuantity(product.id) }}</strong>
          <button
            type="button"
            :disabled="getSelectedQuantity(product.id) >= product.stockQuantity"
            @click="increaseQuantity(product)"
          >
            +
          </button>
        </div>

        <button
          type="button"
          class="cart-button"
          :disabled="product.stockQuantity <= 0 || getSelectedQuantity(product.id) > product.stockQuantity || addingProductId === product.id"
          @click="handleAddCart(product)"
        >
          {{ addingProductId === product.id ? "담는 중..." : "담기" }}
        </button>
      </article>
    </section>
  </main>
</template>

<style scoped>
.product-page {
  width: min(960px, calc(100% - 32px));
  margin: 40px auto;
  color: #1f2937;
}

.product-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
}

.product-header h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
}

.product-list {
  display: grid;
  gap: 12px;
}

.product-item {
  display: grid;
  grid-template-columns: 1fr 160px 132px 92px;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #ffffff;
}

.product-info h2 {
  margin: 0 0 8px;
  font-size: 18px;
}

.product-info p,
.product-price small {
  margin: 0;
  color: #6b7280;
}

.product-price {
  display: grid;
  justify-items: end;
  gap: 4px;
}

.product-price span {
  font-weight: 700;
}

.quantity-control {
  display: grid;
  grid-template-columns: 40px 44px 40px;
  align-items: center;
  justify-content: end;
  gap: 4px;
}

.quantity-control strong {
  text-align: center;
}

.empty-state {
  padding: 32px;
  border-radius: 8px;
  text-align: center;
}

.empty-state {
  border: 1px dashed #d1d5db;
  color: #6b7280;
}

.toast {
  position: fixed;
  top: 24px;
  right: 24px;
  z-index: 10;
  max-width: min(360px, calc(100% - 48px));
  padding: 16px 18px;
  border-radius: 8px;
  background: #ecfdf5;
  box-shadow: 0 12px 24px rgb(17 24 39 / 14%);
  color: #047857;
  font-weight: 700;
}

.toast-error {
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
.quantity-control button {
  padding: 0 14px;
  background: #f3f4f6;
  color: #374151;
}

.cart-button {
  padding: 0 18px;
  background: #111827;
  color: #ffffff;
}

@media (max-width: 760px) {
  .product-header,
  .product-item {
    align-items: stretch;
  }

  .product-header {
    flex-direction: column;
  }

  .product-item {
    grid-template-columns: 1fr;
  }

  .product-price,
  .quantity-control {
    justify-items: start;
    justify-content: start;
  }
}
</style>
