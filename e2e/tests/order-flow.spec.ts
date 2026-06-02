import { test, expect } from "@playwright/test";

test("상품 목록을 확인한다.", async ({ page }) => {
  await page.goto("/products");

  await expect(page.getByRole("heading"), { name: "상품 목록1" }).toBeVisible;
  await expect(page.getByText("노트북")).toBeVisible;
  await expect(page.getByRole("button", { name: "담기" }).first()).toBeVisible();
});
