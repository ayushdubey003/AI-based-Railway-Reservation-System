# Automatically created by: shub deploy

from setuptools import setup, find_packages

setup(
    name         = 'PNRStatusScraper',
    version      = '1.3',
    packages     = find_packages(),
    package_data={
        'PNRStatusScraper': ['resources/*.txt']
    },
    entry_points = {'scrapy': ['settings = PNRStatusScraper.settings']},
)
