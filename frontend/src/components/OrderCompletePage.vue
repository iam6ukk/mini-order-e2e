<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const moveToOrders = () => {
  router.push("/orders");
};

const moveToProducts = () => {
  router.push("/products");
};
</script>

<template>
  <main class="complete-page">
    <header class="complete-header">
      <h1>주문 완료</h1>
    </header>

    <section class="complete-panel">
      <h2>주문이 완료되었습니다</h2>
      <p>주문 내역에서 완료된 주문 정보를 확인할 수 있습니다.</p>

      <div class="complete-actions">
        <button type="button" class="order-button" @click="moveToOrders">주문 내역 보기</button>
        <button type="button" class="secondary-button" @click="moveToProducts">계속 쇼핑하기</button>
      </div>
    </section>
  </main>
</template>

<style scoped>
.complete-page {
  width: min(960px, calc(100% - 32px));
  margin: 40px auto;
  color: #1f2937;
}

.complete-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.complete-header h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
}

.complete-panel {
  padding: 32px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #ffffff;
}

.complete-panel h2 {
  margin: 0 0 8px;
  font-size: 22px;
}

.complete-panel p {
  margin: 0;
  color: #6b7280;
}

.complete-actions {
  display: flex;
  gap: 8px;
  margin-top: 24px;
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

.order-button {
  min-width: 160px;
  padding: 0 24px;
  background: #111827;
  color: #ffffff;
}

@media (max-width: 640px) {
  .complete-header,
  .complete-actions {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
