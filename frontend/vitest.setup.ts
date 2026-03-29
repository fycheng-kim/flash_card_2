import * as matchers from '@testing-library/jest-dom/matchers';
import { expect } from 'vitest';

// This connects the @testing-library matchers to Vitest
expect.extend(matchers);