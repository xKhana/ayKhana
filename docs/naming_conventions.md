# Developer Documentation
## Naming Conventions
### Branch Naming
- General Branch Naming
   - Name of the branch only uses `_`, no other special characters( e.g. `-`), emojis, or capital letters.
  ```
  <scope>/<issue type>/#<issue number>/<subtitle>
  <scope> can be fe, be, ai, gen, ci, doc, test.
  <issue type> can be fix, feat, config, ref, typo, doc.
  ```
- **Frontend Branches:**
  - Should be named as `fe/feat/#<issue number>/subtitle_for_the_branch`.
  - Example: `fe/feat/#123/implement_user_authentication`.

- **Backend Branches:**
  - Should be named as `be/fix/#<issue number>/subtitle-for-the-branch`.
  - Example: `be/fix/#456/implement_api_endpoints`.


### Issue Naming
<!--TODO: rewrite it-->
- **Bug Reports:** 
```
  [<ISSUE TYPE>] Subtitle for The Issue
  <ISSUE TYPE> can be BUG, FEATURE, CONFIG, REFACTOR, TYPO, DOC.
  ```
  - Should be named as `[BUG] Subtitle for the Issue`.
  - Example: `[BUG] Application Crashes on Startup`.

- **Feature Requests:**
  - Should be named as `[FEATURE] Subtitle for The Feature`.
  - Example: `[FEATURE] Implement User Profile Page`.

### Commit Naming

- **General Commits:**
  - Start with a capital letter.
  - Use the imperative mood.
  - Limit the length to 72 characters (length of 50 is highly recommended).
  - See [`<branch name>`](#branch-naming) section.
  ```
  <branch name>: <Commit subject>
  ```
- **Example:**
  ```
  git commit -m "be/feat/#456/implement_api_endpoints: Add authentication middleware"
  ```
- In Case of Committing to Main Branch
  - General Naming Convention
  ```
  <scope>/<commit type>: <Commit subject>
  <scope> can be fe, be, gen, doc, test.
  <commit type> can be fix, feat, config, ref, typo, doc.
  ```
- **Example:**
  ```
  git commit -m "be/feat: Fix login button"
  ```
### PR Naming
- General PR Naming
  ```
  <scope>-<issue type>-#<issue number>-<subtitle>
  <scope> can be fe, be, gen, doc, test.
  <issue type> can be fix, feat, config, ref, typo, doc.
  ```
- **Frontend PRs:**
  - Should be named as `fe-feat-#<issue number>-subtitle_for_the_branch`.
  - Example: `fe-feat-#123-implement_user_authentication`.

- **Backend PRs:**
  - Should be named as `be-feat-#<issue number>/subtitle-for-the-branch`.
  - Example: `be-feat-#456-implement_api_endpoints`.

### Merge Commit Naming
- See [`<branch name>`](#branch-naming) section.
- General Merge Commit Naming
  ```
  <branch name>: Merge #<prNum>
  ```
- Example: 
  ```
    be/feat/#456/implement_api_endpoints: Merge #457
  ```
---

Author: Medhat Mohammed