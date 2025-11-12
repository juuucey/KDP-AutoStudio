"""
Processes competitor data and AI analysis into scored ideas.
"""

from typing import List, Dict
import json


class IdeaProcessor:
    """Processes and scores book ideas from analysis."""
    
    def __init__(self, scoring_weights: Dict = None):
        """
        Initialize processor with scoring weights.
        
        Args:
            scoring_weights: Dictionary with weights for demand, competition, margin, effort, novelty
        """
        self.weights = scoring_weights or {
            'demand': 0.35,
            'competition': 0.25,
            'margin': 0.15,
            'effort': 0.15,
            'novelty': 0.10
        }
    
    def process_analysis(self, keyword: str, competitors: List[Dict], analysis: Dict) -> List[Dict]:
        """
        Process analysis into scored ideas.
        
        Args:
            keyword: The niche keyword
            competitors: Competitor data
            analysis: AI analysis results
            
        Returns:
            List of idea dictionaries ready for database insertion
        """
        ideas = []
        
        # Main idea from keyword
        main_idea = self._create_idea(keyword, competitors, analysis)
        ideas.append(main_idea)
        
        # Generate variations from suggested angles
        suggested_angles = analysis.get('suggested_angles', [])
        for angle in suggested_angles[:3]:  # Limit to 3 variations
            variation = self._create_idea(angle, competitors, analysis, is_variation=True)
            ideas.append(variation)
        
        return ideas
    
    def _create_idea(self, keyword: str, competitors: List[Dict], analysis: Dict, is_variation: bool = False) -> Dict:
        """Create a single idea dictionary with scores."""
        # Extract scores from analysis
        demand = analysis.get('demand_score', 0.5)
        competition = analysis.get('competition_score', 0.5)
        margin = analysis.get('margin_potential', 0.5)
        effort = analysis.get('effort_required', 0.5)
        novelty = analysis.get('novelty_score', 0.5)
        
        # Calculate overall score
        score = (
            self.weights['demand'] * demand +
            self.weights['competition'] * (1 - competition) +
            self.weights['margin'] * margin +
            self.weights['effort'] * (1 - effort) +
            self.weights['novelty'] * novelty
        )
        
        # Generate title suggestion
        title = keyword.title() if not is_variation else keyword
        
        idea = {
            'keyword': keyword,
            'title': title,
            'subtitle': None,
            'demand': round(demand, 3),
            'competition': round(competition, 3),
            'margin': round(margin, 3),
            'effort': round(effort, 3),
            'novelty': round(novelty, 3),
            'score': round(score, 3),
            'risk': analysis.get('risk_assessment', 'Standard risk'),
            'profitability': analysis.get('profitability_notes', 'Moderate potential'),
            'ai_explanation': analysis.get('ai_explanation', 'Analysis completed.'),
            'status': 'pending'
        }
        
        return idea

