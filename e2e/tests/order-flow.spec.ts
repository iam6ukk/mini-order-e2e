import { test, expect } from "@playwright/test";

test("상품 목록 표시 확인", async ({ page }) => {
  await page.goto("/products");
  await expect(page.getByRole("heading", { name: "상품 목록" })).toBeVisible();
  await expect(page.getByText("노트북")).toBeVisible();
  await expect(page.getByRole("button", { name: "담기" }).first()).toBeVisible();
});

test("장바구니 담기 후 주문하기 흐름 확인 ", async ({ page }) => {
  await page.goto("/products");

  await page.getByRole("button", { name: "담기" }).first().click();
  await expect(page.getByText("장바구니에 상품이 추가되었습니다.")).toBeVisible();

  const cartResponse = page.waitForResponse("**/api/cart/items");
  await page.getByRole("button", { name: "장바구니 이동" }).click();
  await cartResponse;
  await expect(page).toHaveURL(/.*\/cart/);

  await expect(page.getByRole("heading", { name: "장바구니" })).toBeVisible();
  await expect(page.getByText("노트북")).toBeVisible();

  const orderResponse = page.waitForResponse("**/api/orders");
  await page.getByRole("button", { name: "주문하기" }).click();
  await orderResponse;
  await page.waitForURL(/.*\/orders\/complete/);
  await expect(page.getByRole("heading", { name: "주문이 완료되었습니다" })).toBeVisible();

  await page.getByRole("button", { name: "주문 내역 보기" }).click();
  await expect(page).toHaveURL(/.*\/orders/);
  await expect(page.getByRole("heading", { name: "주문 내역" })).toBeVisible();
  await expect(page.getByText("노트북").first()).toBeVisible();
});
