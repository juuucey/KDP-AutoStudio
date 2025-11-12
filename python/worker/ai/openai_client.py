"""
OpenAI API client for market analysis and content generation.
"""

import json
from typing import List, Dict, Optional
from openai import OpenAI


class OpenAIClient:
    """Client for OpenAI API interactions."""
    
    def __init__(self, api_key: Optional[str] = None):
        """
        Initialize OpenAI client.
        
        Args:
            api_key: OpenAI API key. If None, will try to load from config.
        """
        if api_key:
            self.api_key = api_key
        else:
            # Try to load from config file
            self.api_key = self._load_api_key()
        
        if not self.api_key:
            raise ValueError("OpenAI API key is required")
        
        self.client = OpenAI(api_key=self.api_key)
    
    def _load_api_key(self) -> Optional[str]:
        """Load API key from config file."""
        try:
            import os
            config_path = os.path.expanduser("~/.kdp-autostudio/config.json")
            if os.path.exists(config_path):
                with open(config_path, 'r') as f:
                    config = json.load(f)
                    return config.get('openAIApiKey')
        except Exception as e:
            print(f"Error loading API key: {e}")
        return None
    
    def analyze_niche(self, keyword: str, competitors: List[Dict]) -> Dict:
        """
        Analyze a niche using OpenAI based on competitor data.
        
        Args:
            keyword: The niche keyword
            competitors: List of competitor product data
            
        Returns:
            Analysis dictionary with insights, scores, and suggestions
        """
        prompt = self._build_analysis_prompt(keyword, competitors)
        
        try:
            response = self.client.chat.completions.create(
                model="gpt-4-turbo-preview",
                messages=[
                    {"role": "system", "content": "You are an expert Amazon KDP market analyst."},
                    {"role": "user", "content": prompt}
                ],
                temperature=0.7
            )
            
            result_text = response.choices[0].message.content
            return self._parse_analysis(result_text, keyword, competitors)
            
        except Exception as e:
            print(f"Error calling OpenAI API: {e}")
            return self._default_analysis(keyword, competitors)
    
    def _build_analysis_prompt(self, keyword: str, competitors: List[Dict]) -> str:
        """Build the analysis prompt for OpenAI."""
        competitor_summary = "\n".join([
            f"- {c.get('title', 'N/A')}: ${c.get('price', 0):.2f}, "
            f"{c.get('rating', 0):.1f} stars, {c.get('review_count', 0)} reviews"
            for c in competitors[:10]
        ])
        
        return f"""Analyze the Amazon KDP niche for keyword: "{keyword}"

Competitor Data:
{competitor_summary}

Please provide a JSON analysis with:
1. market_insights: Why this niche sells or doesn't sell
2. demand_score: 0-1 score for market demand
3. competition_score: 0-1 score for competition level (higher = more competitive)
4. margin_potential: 0-1 score for profit margin potential
5. effort_required: 0-1 score for production effort (higher = more effort)
6. novelty_score: 0-1 score for uniqueness/novelty
7. risk_assessment: Text description of risks
8. profitability_notes: Text description of profit potential
9. suggested_angles: List of 3-5 book angle suggestions
10. ai_explanation: Detailed explanation of the analysis

Return ONLY valid JSON, no markdown formatting."""
    
    def _parse_analysis(self, result_text: str, keyword: str, competitors: List[Dict]) -> Dict:
        """Parse OpenAI response into structured analysis."""
        try:
            # Try to extract JSON from response
            import re
            json_match = re.search(r'\{.*\}', result_text, re.DOTALL)
            if json_match:
                analysis = json.loads(json_match.group(0))
                return analysis
        except Exception as e:
            print(f"Error parsing analysis: {e}")
        
        return self._default_analysis(keyword, competitors)
    
    def _default_analysis(self, keyword: str, competitors: List[Dict]) -> Dict:
        """Generate default analysis if API call fails."""
        avg_price = sum(c.get('price', 0) for c in competitors) / max(len(competitors), 1)
        avg_rating = sum(c.get('rating', 0) for c in competitors) / max(len(competitors), 1)
        
        return {
            "market_insights": f"Moderate competition with {len(competitors)} competitors found.",
            "demand_score": 0.6,
            "competition_score": 0.5,
            "margin_potential": 0.4,
            "effort_required": 0.5,
            "novelty_score": 0.5,
            "risk_assessment": "Standard market risk",
            "profitability_notes": f"Average price: ${avg_price:.2f}, Average rating: {avg_rating:.1f}",
            "suggested_angles": [f"{keyword} - Premium Edition", f"{keyword} - Beginner's Guide"],
            "ai_explanation": "Analysis based on competitor data."
        }
    
    def generate_metadata(self, idea: Dict) -> Dict:
        """
        Generate KDP metadata (title, subtitle, keywords, description, etc.)
        
        Args:
            idea: Idea dictionary with keyword and other attributes
            
        Returns:
            Metadata dictionary
        """
        prompt = f"""Generate complete KDP metadata for a book about: {idea.get('keyword', 'N/A')}

Provide JSON with:
- title: Main title (max 200 chars)
- subtitle: Subtitle (max 200 chars)
- keywords: Array of 7 KDP keywords
- description: Book description (500-2000 chars)
- bisac_categories: Array of 2 BISAC category codes
- blurb: Short marketing blurb (100-200 chars)

Return ONLY valid JSON."""
        
        try:
            response = self.client.chat.completions.create(
                model="gpt-4-turbo-preview",
                messages=[
                    {"role": "system", "content": "You are an expert KDP metadata writer."},
                    {"role": "user", "content": prompt}
                ],
                temperature=0.8
            )
            
            result_text = response.choices[0].message.content
            import re
            json_match = re.search(r'\{.*\}', result_text, re.DOTALL)
            if json_match:
                return json.loads(json_match.group(0))
        except Exception as e:
            print(f"Error generating metadata: {e}")
        
        # Default metadata
        return {
            "title": idea.get('keyword', 'Book Title'),
            "subtitle": "A Comprehensive Guide",
            "keywords": [idea.get('keyword', '')] * 7,
            "description": f"Discover {idea.get('keyword', 'this topic')} in this comprehensive guide.",
            "bisac_categories": ["BUS000000", "REF000000"],
            "blurb": f"Explore {idea.get('keyword', 'this topic')} with this essential guide."
        }

