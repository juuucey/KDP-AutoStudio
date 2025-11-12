"""
Amazon KDP competitor scraper using Playwright.
"""

import time
from typing import List, Dict
from playwright.sync_api import sync_playwright, Browser, Page


class AmazonScraper:
    """Scrapes Amazon product listings for KDP competitor analysis."""
    
    def __init__(self):
        self.base_url = "https://www.amazon.com"
        self.respect_robots_txt = True
        
    def scrape_keyword(self, keyword: str, max_results: int = 20) -> List[Dict]:
        """
        Scrapes Amazon search results for a given keyword.
        
        Args:
            keyword: Search keyword
            max_results: Maximum number of results to return
            
        Returns:
            List of competitor dictionaries with ASIN, title, price, BSR, etc.
        """
        competitors = []
        
        with sync_playwright() as p:
            browser = p.chromium.launch(headless=True)
            page = browser.new_page()
            
            try:
                # Navigate to Amazon search
                search_url = f"{self.base_url}/s?k={keyword.replace(' ', '+')}&i=stripbooks"
                page.goto(search_url, wait_until="networkidle")
                
                # Respect rate limiting
                time.sleep(2)
                
                # Extract product data
                products = page.query_selector_all('[data-asin]')
                
                for product in products[:max_results]:
                    asin = product.get_attribute('data-asin')
                    if not asin or asin == '':
                        continue
                    
                    # Extract title
                    title_elem = product.query_selector('h2 a span')
                    title = title_elem.inner_text() if title_elem else "N/A"
                    
                    # Extract price
                    price_elem = product.query_selector('.a-price .a-offscreen')
                    price = self._parse_price(price_elem.inner_text() if price_elem else None)
                    
                    # Extract rating
                    rating_elem = product.query_selector('.a-icon-alt')
                    rating = self._parse_rating(rating_elem.get_attribute('innerHTML') if rating_elem else None)
                    
                    # Extract review count
                    reviews_elem = product.query_selector('a[href*="#customerReviews"]')
                    review_count = self._parse_review_count(reviews_elem.inner_text() if reviews_elem else None)
                    
                    competitor = {
                        'asin': asin,
                        'title': title,
                        'price': price,
                        'rating': rating,
                        'review_count': review_count,
                        'keyword': keyword
                    }
                    
                    competitors.append(competitor)
                    
            except Exception as e:
                print(f"Error scraping {keyword}: {e}")
            finally:
                browser.close()
        
        return competitors
    
    def _parse_price(self, price_str: str) -> float:
        """Parse price string to float."""
        if not price_str:
            return 0.0
        try:
            # Remove $ and commas
            cleaned = price_str.replace('$', '').replace(',', '').strip()
            return float(cleaned)
        except:
            return 0.0
    
    def _parse_rating(self, rating_str: str) -> float:
        """Parse rating string to float."""
        if not rating_str:
            return 0.0
        try:
            # Extract number from "4.5 out of 5 stars"
            import re
            match = re.search(r'(\d+\.?\d*)', rating_str)
            if match:
                return float(match.group(1))
        except:
            pass
        return 0.0
    
    def _parse_review_count(self, review_str: str) -> int:
        """Parse review count string to int."""
        if not review_str:
            return 0
        try:
            # Remove commas and extract number
            import re
            cleaned = review_str.replace(',', '').strip()
            match = re.search(r'(\d+)', cleaned)
            if match:
                return int(match.group(1))
        except:
            pass
        return 0

