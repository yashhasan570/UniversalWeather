#!/usr/bin/env python3
"""Reapply this fork's rebrand after each upstream sync (Universal Weather)."""
from pathlib import Path
import re, shutil, sys

ROOT = Path(sys.argv[1] if len(sys.argv) > 1 else '.').resolve()

PAIRS = [
    ('org.breezyweather', 'com.universalweather.app'),
    ('breezyweather.', 'com.universalweather.app.'),
    ('BreezyWeather', 'UniversalWeather'),
    ('Breezy Weather', 'Universal Weather'),
    ('breezy-weather/breezy-weather', 'yashhasan570/UniversalWeather'),
    ('universal.report_issue=', 'universal.report_issue_unused='),
    ('Breezy', 'Universal'),
    ('breezy_weather', 'universal_weather'),
    ('BREEZY_WEATHER', 'UNIVERSAL_WEATHER'),
    ('breezy.', 'universal.'),
    ('IS_BREEZY', 'IS_UNIVERSAL'),
]
SKIP_FILES = {'3RD_PARTY.md', 'LICENSE', 'LICENSE_ADDITIONAL'}
TEXT_EXTS = {'.kt', '.java', '.xml', '.kts', '.gradle', '.properties', '.md', '.txt', '.sq',
             '.sqm', '.json', '.pro', '.yml', '.yaml', '.toml', '.gitignore', '.sh', '.aidl',
             '.c', '.h', '.cpp', '.csv', 'PROPERTIES', ''}
JNI_EXTS = {'.c', '.h', '.cpp'}
DEV_FILES = ['CONTRIBUTING.md', 'CHANGELOG.md', 'CODE_OF_CONDUCT.md', 'HELP.md',
             'INSTALL.md', 'docs/CHANGELOG_4.x.md', 'docs/CHANGELOG_5.x.md',
             'docs/COVERAGE.md', 'docs/DAY_DETAILS.md', 'docs/HOMEPAGE.md',
             'docs/RADAR.md', 'docs/SETTINGS.md', 'docs/SOURCES.md', 'docs/TECHNICAL.md',
             'docs/UPDATES.md', 'docs/FullHomepageScreenshot.png', 'docs/fdroid_client_config.png']

README = """# Universal Weather

Material Design weather app for Android, with a clean home screen, weather warnings, radar, widgets, and weather data from many free sources.

## Features

- Detailed current conditions, hourly and daily forecasts
- Weather alerts and warnings
- Radar, air quality and pollen information (where available)
- Home screen widgets and dash clock support
- Material Design with dark, light and dynamic themes

## Contact

Contact on Telegram: @johnconstantine570

## Sync

`.github/workflows/sync.yml` pulls upstream changes daily and re-applies the Universal Weather branding via `scripts/migrate.py`.
"""

PRIVACY = """# Privacy Policy

Universal Weather does not collect or transmit any personal data on its own. The app only contacts the weather services you choose in settings, and sends them nothing beyond what those services need to answer your request (usually a location you picked).

Contact on Telegram: @johnconstantine570
"""

SHORT = 'Material Design weather app for Android\n'
FULL = """Universal Weather is a Material Design weather app for Android.

- Detailed current conditions, hourly and daily forecasts
- Weather alerts and warnings
- Radar, air quality and pollen information (where available)
- Home screen widgets and dash clock support
- Material Design with dark, light and dynamic themes

Contact on Telegram: @johnconstantine570
"""

GRADLE_PROPS = '''
# Universal Weather app links (used by BuildConfig when the "universal" brand flag is NOT set)
app.report_issue=https://github.com/yashhasan570/UniversalWeather/issues
app.source_code_link=https://github.com/yashhasan570/UniversalWeather
app.releases_link=https://github.com/yashhasan570/UniversalWeather/releases
app.install_instructions_link=https://github.com/yashhasan570/UniversalWeather
app.privacy_policy_link=https://github.com/yashhasan570/UniversalWeather/blob/main/PRIVACY.md
app.matrix_link=https://t.me/johnconstantine570
'''


def sweep():
    n = 0
    for f in ROOT.rglob('*'):
        if not f.is_file() or '.git' in f.parts or 'scripts' in f.parts:
            continue
        rel = str(f.relative_to(ROOT))
        if rel in SKIP_FILES or rel.startswith('.github/workflows/sync'):
            continue
        if f.suffix.lower() not in TEXT_EXTS and f.name not in ('.gitignore', 'gradlew', 'gradlew.bat'):
            continue
        try:
            s = f.read_text(encoding='utf-8')
        except Exception:
            continue
        o = s
        for a, b in PAIRS:
            s = s.replace(a, b)
        s = re.sub(r'https://matrix\.to/[^"\s)]+', 'https://t.me/johnconstantine570', s)
        s = re.sub(r'(<string name="about_matrix">)[^<]*(</string>)', r'\g<1>Telegram\g<2>', s)
        s = s.replace('breezyweather_update_errors.txt', 'universalweather_update_errors.txt')
        s = s.replace('hasProperty("breezy")', 'hasProperty("universal")')
        if s != o:
            f.write_text(s, encoding='utf-8')
            n += 1
    return n


def move_packages():
    for d in sorted([d for d in ROOT.rglob('breezyweather') if d.is_dir() and '.git' not in d.parts]):
        parent = d.parent
        base = parent.parent if parent.name == 'org' else parent
        target = base / 'com/universalweather/app'
        if target.exists():
            for child in list(d.iterdir()):
                shutil.move(str(child), str(target / child.name))
            shutil.rmtree(d, ignore_errors=True)
        else:
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.move(str(d), str(target))
    for org in [o for o in ROOT.rglob('org') if o.is_dir() and '.git' not in o.parts]:
        try:
            if not any(org.iterdir()):
                org.rmdir()
        except Exception:
            pass
    for org in [o for o in ROOT.rglob('breezy') if o.is_dir() and '.git' not in o.parts]:
        try:
            shutil.move(str(org), str(org.parent / 'universal'))
        except Exception:
            pass


def renames():
    rb = ROOT / 'app/src/res_breezy'
    if rb.exists():
        shutil.move(str(rb), str(ROOT / 'app/src/res_universal'))
    bg = ROOT / 'app/build.gradle.kts'
    if bg.exists():
        s = bg.read_text().replace('src/res_breezy', 'src/res_universal')
        bg.write_text(s)
    old = ROOT / 'app/src/main/kotlin/com/universalweather/app/BreezyWeather.kt'
    if old.exists():
        shutil.move(str(old), str(old.parent / 'UniversalWeather.kt'))
    for name in ('BreezyActivity', 'BreezyFragment', 'BreezyViewModel'):
        old = ROOT / f'app/src/main/kotlin/com/universalweather/app/common/activities/{name}.kt'
        if old.exists():
            shutil.move(str(old), str(old.parent / (name.replace('Breezy', 'Universal') + '.kt')))


def strip_self_comments():
    uw = ROOT / 'app/src/main/kotlin/com/universalweather/app/UniversalWeather.kt'
    if uw.exists():
        s = uw.read_text()
        s = re.sub(r'\n    /\*\n     \* /!\\ Changing the below logic.*?\n     \*/\n', '\n', s, flags=re.S)
        uw.write_text(s)


def empty_contributors():
    ck = ROOT / 'app/src/main/kotlin/com/universalweather/app/data/Contributors.kt'
    if not ck.exists():
        return
    s = ck.read_text()
    s = s.replace('val appTranslators = arrayOf(', 'val appTranslators = arrayOf<TranslatorItem>(')

    def empty_block(s, marker):
        if marker not in s:
            return s
        i = s.index(marker)
        j = s.index('(', i)
        depth = 0
        k = j
        while True:
            c = s[k]
            if c == '(':
                depth += 1
            elif c == ')':
                depth -= 1
                if depth == 0:
                    break
            k += 1
        return s[:j + 1] + ')' + s[k + 1:]

    s = empty_block(s, 'val appContributors')
    s = empty_block(s, 'val appTranslators')
    ck.write_text(s)


def docs():
    (ROOT / 'README.md').write_text(README)
    (ROOT / 'PRIVACY.md').write_text(PRIVACY)
    for name in DEV_FILES:
        f = ROOT / name
        if f.exists():
            f.unlink()
    d = ROOT / 'docs'
    if d.exists() and not any(d.iterdir()):
        d.rmdir()
    fl = ROOT / 'fastlane/metadata/android'
    if fl.exists():
        for x in fl.iterdir():
            if x.is_dir() and x.name != 'en-US':
                shutil.rmtree(x, ignore_errors=True)
        en = fl / 'en-US'
        en.mkdir(parents=True, exist_ok=True)
        (en / 'title.txt').write_text('Universal Weather\n')
        (en / 'short_description.txt').write_text(SHORT)
        (en / 'full_description.txt').write_text(FULL)
        for sub in ('changelogs', 'images'):
            sd = en / sub
            if sd.exists():
                shutil.rmtree(sd, ignore_errors=True)
    gp = ROOT / 'gradle.properties'
    s = gp.read_text()
    if 'app.report_issue' not in s:
        gp.write_text(s + GRADLE_PROPS)
    sg = ROOT / 'settings.gradle.kts'
    if sg.exists():
        lines = sg.read_text().splitlines(keepends=True)
        sg.write_text(''.join(l for l in lines
                              if 'breezy-weather-data-sharing-lib' not in l
                              and 'substitute(module' not in l))


if __name__ == '__main__':
    print('sweep:', sweep(), 'files')
    move_packages()
    renames()
    strip_self_comments()
    empty_contributors()
    docs()
    print('rebrand reapplied')
