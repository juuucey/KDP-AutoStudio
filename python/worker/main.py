#!/usr/bin/env python3
"""
KDP AutoStudio Python Worker
Handles web scraping and AI reasoning integration.
"""

import sys
import json
import argparse
from scraper.amazon_scraper import AmazonScraper
from ai.openai_client import OpenAIClient
from database.idea_processor import IdeaProcessor

def main():
    parser = argparse.ArgumentParser(description='KDP AutoStudio Python Worker')
    parser.add_argument('--keywords', type=str, required=True, help='Comma-separated seed keywords')
    parser.add_argument('--openai-key', type=str, help='OpenAI API key (or use config)')
    parser.add_argument('--output', type=str, default='output.json', help='Output file path')
    
    args = parser.parse_args()
    
    keywords = [k.strip() for k in args.keywords.split(',')]
    
    print(f"Starting research for keywords: {keywords}")
    
    # Initialize components
    scraper = AmazonScraper()
    openai_client = OpenAIClient(api_key=args.openai_key)
    processor = IdeaProcessor()
    
    results = []
    
    for keyword in keywords:
        print(f"\nProcessing keyword: {keyword}")
        
        # Scrape Amazon
        print("Scraping Amazon listings...")
        competitors = scraper.scrape_keyword(keyword, max_results=20)
        print(f"Found {len(competitors)} competitors")
        
        # Analyze with OpenAI
        print("Analyzing with OpenAI...")
        analysis = openai_client.analyze_niche(keyword, competitors)
        
        # Process and score ideas
        print("Processing ideas...")
        ideas = processor.process_analysis(keyword, competitors, analysis)
        
        results.extend(ideas)
    
    # Save results
    with open(args.output, 'w') as f:
        json.dump(results, f, indent=2)
    
    print(f"\nResearch complete! Generated {len(results)} ideas.")
    print(f"Results saved to: {args.output}")

if __name__ == '__main__':
    main()

